/* Generated By:JJTree: Do not edit this line. OSelectStatement.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=O,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package com.orientechnologies.orient.core.sql.parser;

import com.orientechnologies.orient.core.command.OCommandContext;
import com.orientechnologies.orient.core.db.ODatabaseDocumentInternal;
import com.orientechnologies.orient.core.db.record.OIdentifiable;
import com.orientechnologies.orient.core.exception.OCommandExecutionException;
import com.orientechnologies.orient.core.id.ORID;
import com.orientechnologies.orient.core.iterator.ORecordIteratorClass;
import com.orientechnologies.orient.core.iterator.ORecordIteratorClassDescendentOrder;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.security.ORole;
import com.orientechnologies.orient.core.metadata.security.ORule;
import com.orientechnologies.orient.core.record.ORecord;
import com.orientechnologies.orient.core.sql.OCommandSQLParsingException;
import com.orientechnologies.orient.core.storage.OStorage;

import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

public class OSelectStatement extends OStatement {

  protected OFromClause  target;

  protected OProjection  projection;

  protected OWhereClause whereClause;

  protected OGroupBy     groupBy;

  protected OOrderBy     orderBy;

  protected OUnwind      unwind;

  protected OSkip        skip;

  protected OLimit       limit;

  protected OStorage.LOCKING_STRATEGY lockRecord = null;

  protected OFetchPlan   fetchPlan;

  protected OLetClause   letClause;

  protected OTimeout     timeout;

  protected Boolean      parallel;

  protected Boolean      noCache;

  public OSelectStatement(int id) {
    super(id);
  }

  public OSelectStatement(OrientSql p, int id) {
    super(p, id);
  }

  private OIdentifier getAlias(OProjectionItem item) {
    if (item.getAlias() != null) {
      return item.getAlias();
    } else {
      return item.getDefaultAlias();
    }

  }

  public OProjection getProjection() {
    return projection;
  }

  public void setProjection(OProjection projection) {
    this.projection = projection;
  }

  public OFromClause getTarget() {
    return target;
  }

  public void setTarget(OFromClause target) {
    this.target = target;
  }

  public OWhereClause getWhereClause() {
    return whereClause;
  }

  public void setWhereClause(OWhereClause whereClause) {
    this.whereClause = whereClause;
  }

  public OGroupBy getGroupBy() {
    return groupBy;
  }

  public void setGroupBy(OGroupBy groupBy) {
    this.groupBy = groupBy;
  }

  public OOrderBy getOrderBy() {
    return orderBy;
  }

  public void setOrderBy(OOrderBy orderBy) {
    this.orderBy = orderBy;
  }

  public OSkip getSkip() {
    return skip;
  }

  public void setSkip(OSkip skip) {
    this.skip = skip;
  }

  public OLimit getLimit() {
    return limit;
  }

  public void setLimit(OLimit limit) {
    this.limit = limit;
  }

  public OStorage.LOCKING_STRATEGY getLockRecord() {
    return lockRecord;
  }

  public void setLockRecord(OStorage.LOCKING_STRATEGY lockRecord) {
    this.lockRecord = lockRecord;
  }

  public OFetchPlan getFetchPlan() {
    return fetchPlan;
  }

  public void setFetchPlan(OFetchPlan fetchPlan) {
    this.fetchPlan = fetchPlan;
  }

  public OLetClause getLetClause() {
    return letClause;
  }

  public void setLetClause(OLetClause letClause) {
    this.letClause = letClause;
  }

  public void toString(Map<Object, Object> params, StringBuilder builder) {

    builder.append("SELECT");
    if (projection != null) {
      builder.append(" ");
      projection.toString(params, builder);
    }
    if (target != null) {
      builder.append(" FROM ");
      target.toString(params, builder);
    }

    if (letClause != null) {
      builder.append(" ");
      letClause.toString(params, builder);
    }

    if (whereClause != null) {
      builder.append(" WHERE ");
      whereClause.toString(params, builder);
    }

    if (groupBy != null) {
      builder.append(" ");
      groupBy.toString(params, builder);
    }

    if (orderBy != null) {
      builder.append(" ");
      orderBy.toString(params, builder);
    }

    if (unwind != null) {
      builder.append(" ");
      unwind.toString(params, builder);
    }

    if (skip != null) {
      skip.toString(params, builder);
    }

    if (limit != null) {
      limit.toString(params, builder);
    }

    if (lockRecord!=null) {
      builder.append(" LOCK ");
      switch (lockRecord){
      case DEFAULT:
        builder.append("DEFAULT");
        break;
      case EXCLUSIVE_LOCK:
        builder.append("RECORD");
        break;
      case SHARED_LOCK:
        builder.append("SHARED");
        break;
      case NONE:
        builder.append("NONE");
        break;
      }
    }

    if (fetchPlan != null) {
      builder.append(" ");
      fetchPlan.toString(params, builder);
    }

    if (timeout != null) {
      timeout.toString(params, builder);
    }

    if (Boolean.TRUE.equals(parallel)) {
      builder.append(" PARALLEL");
    }

    if (Boolean.TRUE.equals(noCache)) {
      builder.append(" NOCACHE");
    }
  }

  public void validate() throws OCommandSQLParsingException {

  }

  private boolean isClassTarget(OFromClause target) {

    return target != null && target.item != null && target.item.identifier != null && target.item.identifier.suffix != null
        && target.item.identifier.suffix.identifier != null;
  }

  private boolean isIndexTarget(OFromClause target) {
    return target != null && target.item != null && target.item.index != null;
  }

  public OQueryCursor execute(OCommandContext ctx) {
    // TODO projections
    return new OQueryCursor(fetchFromTarget(ctx), whereClause, orderBy, calculateSkip(ctx), calculateLimit(ctx), ctx);
  }

  private int calculateLimit(OCommandContext ctx) {
    return -1;// TODO
  }

  private int calculateSkip(OCommandContext ctx) {
    return -1;// TODO
  }

  private Iterator<OIdentifiable> fetchFromTarget(OCommandContext ctx) {
    OFromItem targetItem = target.getItem();
    Iterator<OIdentifiable> result = null;
    if (targetItem.cluster != null) {
      // TODO
    } else if (targetItem.identifier != null) {
      if (targetItem.identifier.isBaseIdentifier()) {
        String className = targetItem.identifier.toString();
        OClass oClass = getDatabase().getMetadata().getSchema().getClass(className);
        if (oClass == null) {
          throw new OCommandExecutionException("Class not found in database schema: " + className);
        }
        if (whereClause != null) {
          Iterable resultIterable = whereClause.fetchFromIndexes(oClass, ctx);
          if (resultIterable != null) {
            result = resultIterable.iterator();
          }
        }
        if (result == null) {
          boolean ascendingOrder = true;// TODO
          result = (Iterator<OIdentifiable>) searchInClasses(oClass, true, ascendingOrder);
        }
      } else {
        Object calculationResult = targetItem.identifier.execute(null, ctx);
        if (calculationResult instanceof Iterable) {
          result = ((Iterable<OIdentifiable>) calculationResult).iterator();
        } else if (calculationResult instanceof OIdentifiable) {
          result = (Iterator) Collections.singleton(calculationResult).iterator();
        } else {
          // TODO
        }
      }
    } else {
      // TODO
    }

    return result;
  }

  protected Iterator<? extends OIdentifiable> searchInClasses(final OClass iCls, final boolean iPolymorphic,
      final boolean iAscendentOrder) {

    final ODatabaseDocumentInternal database = getDatabase();
    database.checkSecurity(ORule.ResourceGeneric.CLASS, ORole.PERMISSION_READ, iCls.getName().toLowerCase(Locale.ENGLISH));

    final ORID[] range = new ORID[2];// TODO
    boolean useCache = false;// TODO
    if (iAscendentOrder)
      return new ORecordIteratorClass<ORecord>(database, database, iCls.getName(), iPolymorphic, useCache, false).setRange(range[0],
          range[1]);
    else
      return new ORecordIteratorClassDescendentOrder<ORecord>(database, database, iCls.getName(), iPolymorphic).setRange(range[0],
          range[1]);
  }
}
/* JavaCC - OriginalChecksum=b26959b9726a8cf35d6283eca931da6b (do not edit this line) */
