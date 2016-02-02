package ru.baccasoft.utils.files;

import com.vaadin.spring.annotation.SpringComponent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Transactional;

import ru.baccasoft.utils.files.FilesService.DeletedStatus;
import ru.baccasoft.utils.files.dto.FileContent;
import ru.baccasoft.utils.files.dto.FileInfo;
import ru.baccasoft.utils.files.dto.FileWithContent;

@SpringComponent
public class FilesDao {
	
	private JdbcTemplate jdbc;
	
	@Autowired
	public void setDatasource( DataSource datasource ) {
		jdbc = new JdbcTemplate( datasource );
	}

	/** достает новый лонг из сиквенса */
	@Transactional
	public long getId() {
		return jdbc.queryForObject( "select nextVal('commons_files_sequence')", Long.class );
	}

	private static final String INSERT_CONTENT =
			"insert into file_content( id, storage_uri, created, prev_version_id ) values (?,?,now(),?)";
	
	private static final String INSERT_CONTENT_WITH_GENERATED_ID = 
			"insert into file_content( "+
			"    id, storage_uri, created, prev_version_id "+
			") values ("+
			"    nextVal('commons_files_sequence')"+
			"    , ?"+
			"    , now()"+
			"    , ?"+
			")"+
			"returning id";
	
	private static final ResultSetExtractor<Long> LONG_EXTRACTOR = new ResultSetExtractor<Long>() {

		@Override
		public Long extractData( ResultSet rs ) throws SQLException, DataAccessException {
			rs.next();
			long result = rs.getLong( 1 );
			if( rs.next() ) {
				throw new RuntimeException( "More that one result found");
			}
			return result;
		}
	};
	
	/** 
	 * Если {@link FileContent#id content.id} больше нуля, идентификатор считается заданным,
	 * иначе он генерируется БД и прописывается в <code>content.id</code>. 
	 */
	@Transactional
	public void createContent( final FileContent content )  {
		
		if( content.id > 0 ) {
			jdbc.update( 
				INSERT_CONTENT,
				new Object[] { content.id, content.storageUri, content.previousVersionId }
			);
		} else {
			content.id = jdbc.query(
				INSERT_CONTENT_WITH_GENERATED_ID,
				new Object[] { content.storageUri, content.previousVersionId },
				LONG_EXTRACTOR );
		}
	}
	
	private static final String INSERT_FILE =
			"insert into file_info ("+
					"    id"+
					"    , created"+
					"    , modified"+
					"    , content_id"+
					"    , deleted"+
					"    , filename"+
					"    , content_type"+
					"    , aux_business_type"+
					") values ("+
					"    ?"+
					"    , now()"+
					"    , now()"+
					"    , ?"+
					"    , ?"+
					"    , ?"+
					"    , ?"+
					"    , ?"+
					")";
	private static final String INSERT_FILE_WITH_GENERATED_ID =
			"insert into file_info ("+
					"    id"+
					"    , created"+
					"    , modified"+
					"    , content_id"+
					"    , deleted"+
					"    , filename"+
					"    , content_type"+
					"    , aux_business_type"+
					") values ("+
					"    nextVal('commons_files_sequence')"+
					"    , now()"+
					"    , now()"+
					"    , ?"+
					"    , ?"+
					"    , ?"+
					"    , ?"+
					"    , ?"+
					")"+
					"returning id";
	
	/** 
	 * Если {@link FileInfo#id info.id} больше нуля, идентификатор считается заданным,
	 * иначе он генерируется БД и прописывается в <code>info.id</code>. 
	 */
	@Transactional
	public void createFile( final FileInfo info )  {
		if( info.id > 0 ) {
			jdbc.update( 
				INSERT_FILE,
				new Object[] { 
					info.id, 
					info.contentId, 
					info.deleted, 
					info.filename, 
					info.contentType, 
					info.auxBusinessType 
				}
			);
		} else {
			info.id = jdbc.query( 
				INSERT_FILE_WITH_GENERATED_ID,
				new Object[] { 
					info.contentId, 
					info.deleted, 
					info.filename, 
					info.contentType, 
					info.auxBusinessType 
				},
				LONG_EXTRACTOR );
		}
	}
	
	private static final String UPDATE_FILE =
			"update file_info set"+
			"    modified = now()"+
			"    , content_id = ?"+
			"    , deleted = ?"+
			"    , filename = ?"+
			"    , content_type = ?"+
			"    , aux_business_type = ?"+
			"where"+
			"    id = ?"+
			"    and ("+
			"        content_id != ?"+
			"        or deleted != ?"+
			"        or filename != ?"+
			"        or content_type != ?"+
			"        or aux_business_type != ?"+
			"    ) ";			
	@Transactional
	public void updateFile( final FileInfo info )  {
		jdbc.update( 
			UPDATE_FILE,
			new PreparedStatementSetter() {
				@Override
				public void setValues( PreparedStatement ps ) throws SQLException {
					int idx = 0;
					ps.setLong( ++idx, info.contentId );
					ps.setBoolean( ++idx, info.deleted );
					ps.setString( ++idx, info.filename );
					ps.setString( ++idx, info.contentType );
					ps.setString( ++idx, info.auxBusinessType );

					ps.setLong( ++idx, info.id );

					ps.setLong( ++idx, info.contentId );
					ps.setBoolean( ++idx, info.deleted );
					ps.setString( ++idx, info.filename );
					ps.setString( ++idx, info.contentType );
					ps.setString( ++idx, info.auxBusinessType );
				}
			}
		);
	}
	
	@Transactional
	public void deleteFile( final long id ) {
		jdbc.update( 
			"update file_info set modified = now(), deleted = true where id = ? and not deleted",
			new PreparedStatementSetter() {
				@Override
				public void setValues( PreparedStatement ps ) throws SQLException {
					ps.setLong( 1, id );
				}
			}
		);
	}
	
	private static final String SELECT_FILE_WITH_CONTENT = 
			"select"+
			"    f.id"+
			"    , f.content_id"+
			"    , f.deleted"+
			"    , f.filename"+
			"    , f.content_type"+
			"    , f.aux_business_type"+
			"    , c.prev_version_id"+
			"    , c.storage_uri"+
			" from"+
			"    file_info f"+
			"    inner join file_content c on f.content_id = c.id ";
	
	private static final RowMapper<FileWithContent> FILE_AND_CONTENT_MAPPER = new RowMapper<FileWithContent>() {

		@Override
		public FileWithContent mapRow( ResultSet rs, int rowNum ) throws SQLException {
			int idx = 0;
			FileWithContent result = new FileWithContent();
			result.id = rs.getLong( ++idx );
			result.contentId = rs.getLong( ++idx );
			result.deleted = rs.getBoolean( ++idx );
			result.filename = rs.getString( ++idx );
			result.contentType = rs.getString( ++idx );
			result.auxBusinessType = rs.getString( ++idx );
			
			result.content = new FileContent();
			result.content.id = result.contentId;
			
			result.content.previousVersionId = rs.getLong( ++idx );
			if ( rs.wasNull() ) {
				result.content.previousVersionId = null;
			}
			result.content.storageUri = rs.getString( ++idx );
			
			return result;
		}
	};

	public FileWithContent getFile( long id ) {
		List<FileWithContent> result = 
			new QueryBuilder( SELECT_FILE_WITH_CONTENT )
				.appendClause( new QueryBuilder.NumericEquals( "f.id", id ) )
				.query( jdbc, FILE_AND_CONTENT_MAPPER );
		switch( result.size() ) {
		case 0: return null;
		case 1: return result.iterator().next();
		default:
			throw new RuntimeException( "Expected exactly one result, obtained "+result);
		}
	}

	private static final String SELECT_ID = "select id from file_info";
	
	private static final RowMapper<Long> LONG_MAPPER = new RowMapper<Long>() {
		@Override
		public Long mapRow( ResultSet rs, int rowNum ) throws SQLException {
			return rs.getLong( 1 );
		}
	};
	
	public List<FileWithContent> getArbitraryFiles( DeletedStatus deletedStatus, Date since, String...auxTypes ) {
		return 
			new QueryBuilder( SELECT_FILE_WITH_CONTENT )
				.appendClause( new QueryBuilder.DeletedClause( "f.deleted", deletedStatus ) )
				.appendClause( new QueryBuilder.Since( "f.modified", since ) )
				.appendClause( new QueryBuilder.In( "f.aux_business_type", auxTypes ) )
				.query( jdbc, FILE_AND_CONTENT_MAPPER );
	}
	
	public List<Long> getArbitraryIds( DeletedStatus deletedStatus, Date since, String...auxTypes ) {
		return 
			new QueryBuilder( SELECT_ID )
				.appendClause( new QueryBuilder.DeletedClause( "deleted", deletedStatus ) )
				.appendClause( new QueryBuilder.Since( "modified", since ) )
				.appendClause( new QueryBuilder.In( "aux_business_type", auxTypes ) )
				.query( jdbc, LONG_MAPPER );
	}
}
