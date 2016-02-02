/**
 * Autogenerated by Thrift Compiler (0.9.2)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package ru.baccasoft.misc.thrift.common_2015_08.generated;

import org.apache.thrift.scheme.IScheme;
import org.apache.thrift.scheme.SchemeFactory;
import org.apache.thrift.scheme.StandardScheme;

import org.apache.thrift.scheme.TupleScheme;
import org.apache.thrift.protocol.TTupleProtocol;
import org.apache.thrift.protocol.TProtocolException;
import org.apache.thrift.EncodingUtils;
import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.server.AbstractNonblockingServer.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.Set;
import java.util.HashSet;
import java.util.EnumSet;
import java.util.Collections;
import java.util.BitSet;
import java.nio.ByteBuffer;
import java.util.Arrays;
import javax.annotation.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked"})
@Generated(value = "Autogenerated by Thrift Compiler (0.9.2)", date = "2015-11-27")
public class ThriftTime implements org.apache.thrift.TBase<ThriftTime, ThriftTime._Fields>, java.io.Serializable, Cloneable, Comparable<ThriftTime> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("ThriftTime");

  private static final org.apache.thrift.protocol.TField HHMMSS_FIELD_DESC = new org.apache.thrift.protocol.TField("hhmmss", org.apache.thrift.protocol.TType.STRING, (short)1);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new ThriftTimeStandardSchemeFactory());
    schemes.put(TupleScheme.class, new ThriftTimeTupleSchemeFactory());
  }

  private String hhmmss; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    HHMMSS((short)1, "hhmmss");

    private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

    static {
      for (_Fields field : EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // HHMMSS
          return HHMMSS;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final String _fieldName;

    _Fields(short thriftId, String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.HHMMSS, new org.apache.thrift.meta_data.FieldMetaData("hhmmss", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(ThriftTime.class, metaDataMap);
  }

  public ThriftTime() {
  }

  public ThriftTime(
    String hhmmss)
  {
    this();
    this.hhmmss = hhmmss;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public ThriftTime(ThriftTime other) {
    if (other.isSetHhmmss()) {
      this.hhmmss = other.hhmmss;
    }
  }

  public ThriftTime deepCopy() {
    return new ThriftTime(this);
  }

  @Override
  public void clear() {
    this.hhmmss = null;
  }

  public String getHhmmss() {
    return this.hhmmss;
  }

  public ThriftTime setHhmmss(String hhmmss) {
    this.hhmmss = hhmmss;
    return this;
  }

  public void unsetHhmmss() {
    this.hhmmss = null;
  }

  /** Returns true if field hhmmss is set (has been assigned a value) and false otherwise */
  public boolean isSetHhmmss() {
    return this.hhmmss != null;
  }

  public void setHhmmssIsSet(boolean value) {
    if (!value) {
      this.hhmmss = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case HHMMSS:
      if (value == null) {
        unsetHhmmss();
      } else {
        setHhmmss((String)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case HHMMSS:
      return getHhmmss();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case HHMMSS:
      return isSetHhmmss();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof ThriftTime)
      return this.equals((ThriftTime)that);
    return false;
  }

  public boolean equals(ThriftTime that) {
    if (that == null)
      return false;

    boolean this_present_hhmmss = true && this.isSetHhmmss();
    boolean that_present_hhmmss = true && that.isSetHhmmss();
    if (this_present_hhmmss || that_present_hhmmss) {
      if (!(this_present_hhmmss && that_present_hhmmss))
        return false;
      if (!this.hhmmss.equals(that.hhmmss))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    List<Object> list = new ArrayList<Object>();

    boolean present_hhmmss = true && (isSetHhmmss());
    list.add(present_hhmmss);
    if (present_hhmmss)
      list.add(hhmmss);

    return list.hashCode();
  }

  @Override
  public int compareTo(ThriftTime other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetHhmmss()).compareTo(other.isSetHhmmss());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetHhmmss()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.hhmmss, other.hhmmss);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("ThriftTime(");
    boolean first = true;

    sb.append("hhmmss:");
    if (this.hhmmss == null) {
      sb.append("null");
    } else {
      sb.append(this.hhmmss);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // check for sub-struct validity
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
    try {
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class ThriftTimeStandardSchemeFactory implements SchemeFactory {
    public ThriftTimeStandardScheme getScheme() {
      return new ThriftTimeStandardScheme();
    }
  }

  private static class ThriftTimeStandardScheme extends StandardScheme<ThriftTime> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, ThriftTime struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // HHMMSS
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.hhmmss = iprot.readString();
              struct.setHhmmssIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, ThriftTime struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.hhmmss != null) {
        oprot.writeFieldBegin(HHMMSS_FIELD_DESC);
        oprot.writeString(struct.hhmmss);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class ThriftTimeTupleSchemeFactory implements SchemeFactory {
    public ThriftTimeTupleScheme getScheme() {
      return new ThriftTimeTupleScheme();
    }
  }

  private static class ThriftTimeTupleScheme extends TupleScheme<ThriftTime> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, ThriftTime struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetHhmmss()) {
        optionals.set(0);
      }
      oprot.writeBitSet(optionals, 1);
      if (struct.isSetHhmmss()) {
        oprot.writeString(struct.hhmmss);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, ThriftTime struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(1);
      if (incoming.get(0)) {
        struct.hhmmss = iprot.readString();
        struct.setHhmmssIsSet(true);
      }
    }
  }

}

