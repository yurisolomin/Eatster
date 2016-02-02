package ru.baccasoft.utils.files;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import ru.baccasoft.utils.files.FilesService.DeletedStatus;

/**
 * Простенькая автоматизация select-ов c хитровывернутыми where.
 */
class QueryBuilder {
	
	private static final String WHERE = " where ";
	private static final String AND = " and ";
	private static Object[] OBJ_ARRAY = new Object[] {};

	private StringBuilder sql = new StringBuilder();
	private List<Object> args = new ArrayList<Object>();
	private String separator = WHERE;
	
	interface IPredicate {
		boolean isActive();
		String clauseSql();
		void appendToArgs( List<Object> params );
	}
	
	QueryBuilder( String baseSql ) {
		sql.append( baseSql );
	}
	
	QueryBuilder appendClause( IPredicate clause ) {
		if( clause.isActive() ) {
			sql.append( separator ).append( clause.clauseSql() );
			separator = AND;
			clause.appendToArgs( args );
		}
		return this;
	}
	
	<Result> List<Result> query( JdbcTemplate jdbc, RowMapper<Result> mapper ) {
		Object[] args = this.args.toArray( OBJ_ARRAY );
		return jdbc.query( sql.toString(), args, mapper );
	}
	
	static class DeletedClause implements IPredicate {
		private final DeletedStatus status;
		private final String attribute;
		
		DeletedClause( String attribute, DeletedStatus status ) {
			this.status = status;
			this.attribute = attribute;
		}
		
		DeletedClause( DeletedStatus status ) {
			this( "deleted", status );
		}
		
		@Override
		public boolean isActive() { return status != DeletedStatus.ANY; }
		
		@Override
		public String clauseSql() {
			switch( status ) {
			case ACTIVE_ONLY: return "not "+attribute;
			case DELETED_ONLY: return attribute;
			default:
				throw new IllegalStateException( "Illegal status "+status );
			}
		}

		@Override
		public void appendToArgs( List<Object> params ) {}
	}
	
	static class Since implements IPredicate {
		private final String attribute;
		private final Date since;
		Since( String attribute, Date since ) {
			this.attribute = attribute;
			this.since = since;
		}
		
		@Override
		public boolean isActive() { return since != null; }
		
		@Override
		public String clauseSql() {
			return attribute + " >= ?";
		}
		
		@Override
		public void appendToArgs( List<Object> params ) {
			params.add( new Timestamp( since.getTime() ) );
		}
	}

	static class NumericEquals implements IPredicate {
		private final String attribute;
		private final long value;
		
		NumericEquals( String attribute, long value ) {
			this.attribute = attribute;
			this.value = value;
		}
		
		@Override
		public boolean isActive() { return true; }
		
		@Override
		public String clauseSql() {
			return attribute + " = ?";
		}
		
		@Override
		public void appendToArgs( List<Object> params ) {
			params.add( value );
		}
	}
	static class In implements IPredicate {
		private final String attribute;
		private final String [] args;
		In( String attribute, String [] args ) {
			this.attribute = attribute;
			this.args = args;
		}
		@Override
		public boolean isActive() {
			return args.length > 0;
		}
		@Override
		public String clauseSql() {
			StringBuilder buff = new StringBuilder();
			buff.append( attribute );
			if( args.length == 1 ) {
				buff.append( " = ?");
			} else {
				buff.append( " in (?" );
				for( int i = 1; i < args.length; ++i ) {
					buff.append( ",?");
				}
				buff.append( ')');
			}
			return buff.toString();
		}
		@Override
		public void appendToArgs( List<Object> params ) {
			for( String arg: args ) {
				params.add( arg );
			}
		}
	}
}
