package name.edward.elasticsearch.core;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.bulk.byscroll.BulkByScrollResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 封装ES操作
 * 
 * @author edward
 * @date 2017-08-01
 */
public class Executor implements IExecutor {

	private static final Logger LOGGER = LoggerFactory.getLogger(Executor.class);

	@Override
	public boolean createIndex(Client client, String index) {
		LOGGER.info("Starting to create index:=====>{}<=====", index);
		try {
			CreateIndexRequest request = new CreateIndexRequest(index);
			CreateIndexResponse response = client.admin().indices().create(request).actionGet();
			LOGGER.info("The index=====>{}<=====has been created successfully", index);
			return response.isAcknowledged();
		} catch (Exception e) {
			LOGGER.error("Failing to create index:=====>{}<=====due to=====>{}<=====", index, e);
		}
		return false;
	}

	@Override
	public boolean isExisted(Client client, String... indexes) {
		LOGGER.info("Starting to validate given indexes:=====>{}<=====which all exist or not", Arrays.asList(indexes));
		try {
			if (null == indexes || 0 == indexes.length) {
				LOGGER.warn("the array of index is not allowed NULL or empty element");
				return false;
			}
			IndicesExistsRequest inExistsRequest = new IndicesExistsRequest(indexes);
			IndicesExistsResponse indicesExistsResponse = client.admin().indices().exists(inExistsRequest).actionGet();
			return indicesExistsResponse.isExists();
		} catch (Exception e) {
			LOGGER.error("Occoring an exception when tovalidate given indexes due to=====>{}<=====", e);
		}
		return false;
	}

	@Override
	public boolean deleteIndex(Client client, String... indexes) {
		List<String> list = Arrays.asList(indexes);
		LOGGER.info("Starting to delete given indexes:=====>{}<=====", list);
		try {
			if (isExisted(client, indexes)) {
				DeleteIndexResponse response = client.admin().indices().prepareDelete(indexes).execute().actionGet();
				LOGGER.info("the indexes=====>{}<=====has been deleted successfully", list);
				return response.isAcknowledged();
			}
		} catch (Exception e) {
			LOGGER.error("Failing to delete the indexes=====>{}<=====due to=====>{}<=====", list, e);
		}
		return false;
	}

	@Override
	public boolean deleteDocuments(Client client) {
		LOGGER.info("Starting to delete documents");
		try {
			BulkByScrollResponse response = DeleteByQueryAction.INSTANCE.newRequestBuilder(client)
					.filter(QueryBuilders.matchAllQuery()).get();
			LOGGER.info("There are=====>{}<===== bars of document deleted", response.getDeleted());
			return true;
		} catch (Exception e) {
			LOGGER.error("Failing to delete documents due to=====>{}<=====", e);
		}
		return false;
	}

	@Override
	public boolean deleteDocuments(Client client, String... indexes) {
		LOGGER.info("Starting to delete all documents under the indexes:=====>{}<=====", Arrays.asList(indexes));
		try {
			BulkByScrollResponse response = DeleteByQueryAction.INSTANCE.newRequestBuilder(client)
					.filter(QueryBuilders.matchAllQuery()).source(indexes).get();
			LOGGER.info("There are=====>{}<=====bars of document deleted", response.getDeleted());
			return true;
		} catch (Exception e) {
			LOGGER.error("Failing to delete documents due to=====>{}<=====", e);
		}
		return false;
	}

	@Override
	public boolean deleteDocuments(Client client, String index, String type) {
		LOGGER.info("Starting to delete all documents under the indexes:=====>{}<===== and the type:=====>{}<=====",
				index, type);
		try {
			BulkByScrollResponse response = DeleteByQueryAction.INSTANCE.newRequestBuilder(client)
					.filter(QueryBuilders.typeQuery(type)).source(index).get();
			LOGGER.info("There are=====>{}<===== bars of document deleted", response.getDeleted());
			return true;
		} catch (Exception e) {
			LOGGER.error("Failing to delete documents due to=====>{}<=====", e);
		}
		return false;
	}

	@Override
	public boolean createMapping(Client client, String index, String type, Map<String, Object> mappingMap) {
		LOGGER.info("Starting to create a mapping, under the index:=====>{}<=====and the type:=====>{}<=====", index,
				type);
		try {
			PutMappingRequest mapping = Requests.putMappingRequest(index).type(type).source(mappingMap);
			PutMappingResponse response = client.admin().indices().putMapping(mapping).actionGet();
			LOGGER.info("Creating mapping successfully");
			return response.isAcknowledged();
		} catch (Exception e) {
			LOGGER.error("Failing to create mapping=====>{}<=====", e);
		}
		return false;
	}

	@Override
	public String getMapping(Client client, String index, String type) {
		ImmutableOpenMap<String, MappingMetaData> mappings = client.admin().cluster().prepareState().execute()
				.actionGet().getState().getMetaData().getIndices().get(index).getMappings();
		return mappings.get(type).source().toString();
	}

	@Override
	public void batchUpload(Client client, String index, String type, String primaryKey,
			List<Map<String, Object>> dataList) {
		LOGGER.info("Starting to bulk uploadding of documents from data");
		BulkRequestBuilder bulkRequestBuilder = client.prepareBulk();
		for (Map<String, Object> recordMap : dataList) {
			if (null != primaryKey && 0 < primaryKey.length()) {
				bulkRequestBuilder.add(
						client.prepareIndex(index, type, recordMap.get(primaryKey).toString()).setSource(recordMap));
			} else {
				bulkRequestBuilder.add(client.prepareIndex(index, type).setSource(recordMap));
			}
		}
		BulkResponse bulkResponse = bulkRequestBuilder.execute().actionGet();
		if (bulkResponse.hasFailures()) {
			LOGGER.error("A part of documents have been failed to upload due to=====>{}<=====",
					bulkResponse.buildFailureMessage());
		}
		LOGGER.info("Finish bulking uploadding of documents");
	}

}
