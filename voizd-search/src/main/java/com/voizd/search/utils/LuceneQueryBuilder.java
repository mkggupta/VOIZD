package com.voizd.search.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.index.query.FieldQueryBuilder;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.FilteredQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.voizd.common.beans.vo.SearchVO;
import com.voizd.search.dataobject.SearchResponse;


public class LuceneQueryBuilder {
	private static Logger logger = LoggerFactory.getLogger(LuceneQueryBuilder.class);
	public static final List<String> characterDiscardList = Arrays.asList(":",
			")", "(", "{", "}", "[" ,"]", "/", "\\", "/\\", "?", "!", "^","%", "+", "-", ">", "<");
	private static final List<String> excludedtags = Arrays.asList("$tag",
			"select", "NOL", "ENTER_USER_ID", "$name");
	public enum StationContentFieldNameMapping {
		CATEGORYID("categoryId"), STATIONNAME("stationName"),LOCATION("location"), LASTNAME("lastName"), FIRSTNAME("firstName"), DESCRIPTION(
				"description"),LANGUAGE("language"),TITLE("title"),SEARCH("search"), TAG(
				"tag");

		private String value;

		public String getValue() {
			return value;
		}

		private StationContentFieldNameMapping(String value) {
			this.value = value;
		}
	}
	
	public enum FieldValue {

		STARTCATIDFROM("49"), SPACE(" "), BLANK(""), TOMO("TOMO"), YES("Yes"), NO(
				"N0"), MEDIAPRIVACYYES("ACCESS_YES"), MEDIAPRIVACYNO(
				"ACCESS_NO"), MEDIAPRIVACYFRN("ACCESS_FRN"), PUBLIC("PUBLIC"), PUBLICFRN(
				"PUBLICFRN"), PRIVATE("PRIVATE"), ACTIVE("ACTIVE"), PENDING(
				"PENDING"), REMOVEDBYADMIN("REMOVEDBYADMIN"), FRN("FRN"), STANDARD(
				"standard"), SCORE("_score"), STOP("stop");

		private String value;

		public String getValue() {
			return value;
		}

		private FieldValue(String value) {
			this.value = value;
		}

	}

	
/*	public static String getStationQuery(String tag,int s,int end) {

		//String tag = criteria.getCriteria().get(QueryParameter.TAG.toString());		
		logger.error("getQuery() ::  tag ::"+tag);
		
		tag = isEmpty(tag) ? "*" : buildTagExpression(tag);		
		logger.error("getQuery() ::  tag 1 ::"+tag);

	
		QueryBuilder queryBuilder = QueryBuilders.fieldQuery(
				StationContentFieldNameMapping.TAG.getValue(), tag).analyzer(
				FieldValue.STANDARD.getValue()).analyzer(
						FieldValue.STOP.getValue());

		FilterBuilder matchFilterBuilder = FilterBuilders.matchAllFilter();

		FilteredQueryBuilder filteredQueryBuilder = QueryBuilders
				.filteredQuery(queryBuilder, matchFilterBuilder);

		
		logger.error("getQuery() ::  filteredQueryBuilder.toString()  ::"+filteredQueryBuilder.toString());
		String q =getQuerydata(createQueryWithBuilderWithoutBrackets(filteredQueryBuilder.toString(), s,end));
		logger.error("getQuery() :: string  ::"+q);
		return q;

	}*/
	/*public static String getStationQuery(String tag,int s,int end) {

		//String tag = criteria.getCriteria().get(QueryParameter.TAG.toString());		
		logger.error("getQuery() ::  tag ::"+tag);
		
		tag = isEmpty(tag) ? "*" : buildTagExpression(tag);		
		logger.error("getQuery() ::  tag 1 ::"+tag);

	
		QueryBuilder queryBuilder = QueryBuilders.fieldQuery(
				StationContentFieldNameMapping.TAG.getValue(), tag).analyzer(
				FieldValue.STANDARD.getValue()).analyzer(
						FieldValue.STOP.getValue());
		
		//QueryBuilder queryBuilder = QueryBuilders.

		FilterBuilder matchFilterBuilder = FilterBuilders.matchAllFilter();
	    String ok = QueryBuilders.fieldQuery(tag, true).toString();
		FilteredQueryBuilder filteredQueryBuilder = QueryBuilders
				.filteredQuery(tag, matchFilterBuilder);

		
		logger.error("getQuery() ::  filteredQueryBuilder.toString()  ::"+ok);
		String q =getQuerydata(createQueryWithBuilderWithoutBrackets(ok, s,end));
		logger.error("getQuery() :: string  ::"+q);
		return q;

	}*/
	
	/*public static String getQuery(String tag) {
			tag = isEmpty(tag) ? "*" : buildTagExpression(tag);		
		QueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(tag,StationContentFieldNameMapping.TAG.getValue(),StationContentFieldNameMapping.DESCRIPTION.getValue());
		FilterBuilder matchFilterBuilder = FilterBuilders.matchAllFilter();
		FilteredQueryBuilder filteredQueryBuilder = QueryBuilders.filteredQuery(queryBuilder, matchFilterBuilder);
		return getQuerydata(createQueryWithBuilderWithoutBrackets(filteredQueryBuilder.toString(),0,20));

	}*/
	
	/*public static String getStationQuery(String tag,int startLimit,int endLimit) {
		tag = isEmpty(tag) ? "*" : buildTagExpression(tag);
		QueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(tag, StationContentFieldNameMapping.TAG.getValue(),
				StationContentFieldNameMapping.DESCRIPTION.getValue(),StationContentFieldNameMapping.FIRSTNAME.getValue(),StationContentFieldNameMapping.LASTNAME.getValue(),
				StationContentFieldNameMapping.LOCATION.getValue(),StationContentFieldNameMapping.LANGUAGE.getValue(),StationContentFieldNameMapping.STATIONNAME.getValue());
		FilterBuilder matchFilterBuilder = FilterBuilders.matchAllFilter();
		FilteredQueryBuilder filteredQueryBuilder = QueryBuilders.filteredQuery(queryBuilder, matchFilterBuilder);
		return getQuerydata(createQueryWithBuilderWithoutBrackets(filteredQueryBuilder.toString(), startLimit,endLimit));

	}*/
	
	/*public static String getStationQuery(String tag,int startLimit,int endLimit) {
		tag = isEmpty(tag) ? "*" : buildTagExpression(tag);
		QueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(tag, StationContentFieldNameMapping.TAG.getValue(),
				StationContentFieldNameMapping.DESCRIPTION.getValue(),StationContentFieldNameMapping.FIRSTNAME.getValue(),StationContentFieldNameMapping.LASTNAME.getValue(),
				StationContentFieldNameMapping.LOCATION.getValue(),StationContentFieldNameMapping.LANGUAGE.getValue(),
				StationContentFieldNameMapping.STATIONNAME.getValue()).analyzer(
						FieldValue.STANDARD.getValue()).analyzer(
								FieldValue.STOP.getValue());
		QueryBuilder queryBuilder = QueryBuilders.fieldQuery(StationContentFieldNameMapping.STATIONNAME.getValue(), tag)
				.analyzer(FieldValue.STANDARD.getValue()).analyzer(FieldValue.STOP.getValue());
				QueryBuilders.fieldQuery(
				StationContentFieldNameMapping.STATIONNAME.getValue(), tag).analyzer(
				FieldValue.STANDARD.getValue()).analyzer(
						FieldValue.STOP.getValue());
		//tag = isEmpty(tag) ? "*" : buildTagExpression(tag);
		QueryBuilder queryBuilder2 = QueryBuilders.fieldQuery(
				StationContentFieldNameMapping.FIRSTNAME.getValue(), tag).analyzer(
				FieldValue.STANDARD.getValue()).analyzer(
						FieldValue.STOP.getValue());
	

		FilterBuilder matchFilterBuilder = FilterBuilders.matchAllFilter();
		FilteredQueryBuilder filteredQueryBuilder = QueryBuilders.filteredQuery(queryBuilder,matchFilterBuilder);
		FilteredQueryBuilder filteredQueryBuilder1 = QueryBuilders
				.filteredQuery(queryBuilder2, matchFilterBuilder);
		
	    filteredQueryBuilder = addFirstNameData(tag,filteredQueryBuilder);
	    filteredQueryBuilder = addLastNameData(tag,filteredQueryBuilder);
	    filteredQueryBuilder = addLocationData(tag,filteredQueryBuilder);
	    filteredQueryBuilder = addLaguageData(tag,filteredQueryBuilder);
	  return getQuerydata(createQueryWithBuilderWithoutBrackets(filteredQueryBuilder.toString(), startLimit,endLimit));

	}*/
	
	public static String getAdvanceStationQuery(SearchVO searchVO,int startLimit,int endLimit) {
		String tag = searchVO.getTag();
		tag = isEmpty(tag) ? "*" : buildTagExpression(tag);
		QueryBuilder queryBuilder = QueryBuilders.fieldQuery(
				StationContentFieldNameMapping.TAG.getValue(), tag).analyzer(
				FieldValue.STANDARD.getValue()).analyzer(
						FieldValue.STOP.getValue());

		FilterBuilder matchFilterBuilder = FilterBuilders.matchAllFilter();
		FilteredQueryBuilder filteredQueryBuilder = QueryBuilders
				.filteredQuery(queryBuilder, matchFilterBuilder);
		
		if(searchVO.getUserName() != null){
			String userName = searchVO.getUserName();
			//logger.error(" getAdvanceStationQuery() :: userName :: "+userName);
			filteredQueryBuilder = addFirstNameData(userName,filteredQueryBuilder);
			filteredQueryBuilder = addLastNameData(userName,filteredQueryBuilder);
		}
		
		if(searchVO.getLocation() != null){
			String location = searchVO.getLocation();
			//logger.error(" getAdvanceStationQuery() :: location :: "+location);
			filteredQueryBuilder = addLocationData(location,filteredQueryBuilder);
		}
		if(searchVO.getLanguage() != null){
			String language = searchVO.getLanguage();
			//logger.error(" getAdvanceStationQuery() :: language :: "+language);
		    filteredQueryBuilder = addLaguageData(language,filteredQueryBuilder);
		}
		return getQuerydata(createQueryWithBuilderWithoutBrackets(filteredQueryBuilder.toString(), startLimit,endLimit));

	}
	private static FilteredQueryBuilder addFirstNameData(String userName,
			FilteredQueryBuilder filteredQueryBuilder) {
		if ((isCriteria(userName))) {
			userName = "\"" + userName + "\" " + userName + "*";
			FieldQueryBuilder fieldQueryBuilder = QueryBuilders.fieldQuery(
					StationContentFieldNameMapping.FIRSTNAME.getValue(), userName).analyzer(
							FieldValue.STANDARD.getValue()).boost(2);

			FilterBuilder userFilterBuilder = FilterBuilders
					.queryFilter(fieldQueryBuilder);

			filteredQueryBuilder = QueryBuilders.filteredQuery(
					filteredQueryBuilder, userFilterBuilder);
		}
		return filteredQueryBuilder;
	}
	private static FilteredQueryBuilder addLastNameData(String userName,
			FilteredQueryBuilder filteredQueryBuilder) {
		if ((isCriteria(userName))) {
			userName = "\"" + userName + "\" " + userName + "*";
			FieldQueryBuilder fieldQueryBuilder = QueryBuilders.fieldQuery(
					StationContentFieldNameMapping.LASTNAME.getValue(), userName).analyzer(
							FieldValue.STANDARD.getValue()).boost(2);

			FilterBuilder userFilterBuilder = FilterBuilders
					.queryFilter(fieldQueryBuilder);

			filteredQueryBuilder = QueryBuilders.filteredQuery(
					filteredQueryBuilder, userFilterBuilder);
		}
		return filteredQueryBuilder;
	}
	private static FilteredQueryBuilder addLocationData(String location,
			FilteredQueryBuilder filteredQueryBuilder) {
		if ((isCriteria(location))) {
			location = "\"" + location + "\" " + location + "*";
			FieldQueryBuilder fieldQueryBuilder = QueryBuilders.fieldQuery(
					StationContentFieldNameMapping.LOCATION.getValue(), location).analyzer(
							FieldValue.STANDARD.getValue()).boost(2);

			FilterBuilder userFilterBuilder = FilterBuilders
					.queryFilter(fieldQueryBuilder);

			filteredQueryBuilder = QueryBuilders.filteredQuery(
					filteredQueryBuilder, userFilterBuilder);
		}
		return filteredQueryBuilder;
	}
	
	private static FilteredQueryBuilder addLaguageData(String language,
			FilteredQueryBuilder filteredQueryBuilder) {
		if ((isCriteria(language))) {
			language = "\"" + language + "\" " + language + "*";
			FieldQueryBuilder fieldQueryBuilder = QueryBuilders.fieldQuery(
					StationContentFieldNameMapping.LANGUAGE.getValue(), language).analyzer(
							FieldValue.STANDARD.getValue()).boost(2);

			FilterBuilder userFilterBuilder = FilterBuilders
					.queryFilter(fieldQueryBuilder);

			filteredQueryBuilder = QueryBuilders.filteredQuery(
					filteredQueryBuilder, userFilterBuilder);
		}
		return filteredQueryBuilder;
	}
	
	public static String getContentQueryFull(String tag,int startLimit,int endLimit) {
		tag = isEmpty(tag) ? "*" : buildTagExpression(tag);
		QueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(tag, StationContentFieldNameMapping.TITLE.getValue(),StationContentFieldNameMapping.FIRSTNAME.getValue(),StationContentFieldNameMapping.LASTNAME.getValue());
		FilterBuilder matchFilterBuilder = FilterBuilders.matchAllFilter();
		FilteredQueryBuilder filteredQueryBuilder = QueryBuilders.filteredQuery(queryBuilder, matchFilterBuilder);
		return getQuerydata(createQueryWithBuilderWithoutBrackets(filteredQueryBuilder.toString(), startLimit,endLimit));

	}
	
	public static String getContentQuery(String tag,int startLimit,int endLimit) {
		tag = isEmpty(tag) ? "*" : buildTagExpression(tag);
		QueryBuilder queryBuilder = QueryBuilders.fieldQuery(StationContentFieldNameMapping.TITLE.getValue(), tag).analyzer(
				FieldValue.STANDARD.getValue()).analyzer(
						FieldValue.STOP.getValue());

		FilterBuilder matchFilterBuilder = FilterBuilders.matchAllFilter();
		FilteredQueryBuilder filteredQueryBuilder = QueryBuilders
				.filteredQuery(queryBuilder, matchFilterBuilder);
		List<EsSort> orderList = getOrderList(null);
		String order = generateSearchSortSubQuery(orderList);
		return getQuerydata(createQueryWithBuilderWithoutBrackets(filteredQueryBuilder.toString(), startLimit,endLimit));

	}

	public static String getContentTagQuery(String tag,int startLimit,int endLimit) {
		tag = isEmpty(tag) ? "*" : buildTags(tag);
		QueryBuilder queryBuilder = QueryBuilders.fieldQuery(StationContentFieldNameMapping.SEARCH.getValue(), tag).analyzer(
				FieldValue.STANDARD.getValue());
	
		String data = createQueryWithBuilderWithoutBrackets(queryBuilder.toString(), startLimit,endLimit);
		List<EsSort> orderList = getOrderList(null);
		String order = generateSearchSortSubQuery(orderList);
		return getQuerydata(order+data);

	}

	private static String buildTags(String tag){
		for(String specialCharacter : characterDiscardList){
			tag = tag.replace(specialCharacter, " ");
		}
		StringBuilder str = new StringBuilder();
	//	str.append("");
		str.append(tag);
	//	str.append("" );
		return str.toString();
	}
	public static String getStationQuery(String tag,int startLimit,int endLimit) {	
		tag = isEmpty(tag) ? "*" : buildTag(tag);
		String data="\"query\":{\"query_string\":{\"query\":"+tag+"}}"+", \"from\" :" + startLimit+ ", \"size\" : " + endLimit + "";;
		return getQuerydata(data);

	}
	
	/*public static String getStationQuery(String tag,int startLimit,int endLimit) {
	tag = isEmpty(tag) ? "*" : buildTagExpression(tag);
	QueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(tag, StationContentFieldNameMapping.TAG.getValue(),
			StationContentFieldNameMapping.DESCRIPTION.getValue(),StationContentFieldNameMapping.FIRSTNAME.getValue(),StationContentFieldNameMapping.LASTNAME.getValue(),
			StationContentFieldNameMapping.LOCATION.getValue(),StationContentFieldNameMapping.LANGUAGE.getValue(),StationContentFieldNameMapping.STATIONNAME.getValue());
	FilterBuilder matchFilterBuilder = FilterBuilders.matchAllFilter();
	FilteredQueryBuilder filteredQueryBuilder = QueryBuilders.filteredQuery(queryBuilder, matchFilterBuilder);
	return getQuerydata(createQueryWithBuilderWithoutBrackets(filteredQueryBuilder.toString(), startLimit,endLimit));

}*/
	public static String getAdvanceContentQuery(SearchVO searchVO,int startLimit,int pageLimit) {
		String tag = searchVO.getTag();
		
		tag = isEmpty(tag) ? "*" : buildTagExpression(tag);
		QueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(tag, StationContentFieldNameMapping.TAG.getValue(),
				StationContentFieldNameMapping.DESCRIPTION.getValue());
		FilterBuilder matchFilterBuilder = FilterBuilders.matchAllFilter();
		FilteredQueryBuilder filteredQueryBuilder = QueryBuilders.filteredQuery(queryBuilder, matchFilterBuilder);
		return getQuerydata(createQueryWithBuilderWithoutBrackets(filteredQueryBuilder.toString(), startLimit,pageLimit));

	}


	private static String buildTagExpression (String tag){
		for(String specialCharacter : characterDiscardList){
			tag = tag.replace(specialCharacter, " ");
		}
		StringBuilder str = new StringBuilder();
		str.append("\"");
		str.append(tag);
		str.append("\" " );
		str.append(tag);
		str.append(" ");
		str.append(tag);
		str.append("*");
		str.append(" *");
		str.append(tag);
		str.append("*");
		return str.toString();
	}
	
	private static String buildTag(String tag){
		for(String specialCharacter : characterDiscardList){
			tag = tag.replace(specialCharacter, " ");
		}
		StringBuilder str = new StringBuilder();
		str.append("\"*");
		str.append(tag);
		str.append("*\"");
		return str.toString();
	}
	protected static boolean isEmpty(String value) {
		return (value == null) || "".equals(value.trim());
	}
	
	public static String createQueryWithBuilderWithoutBrackets(
			String queryBuilderValue, int from, int total) {
		from = from >= 0 ? from : 0;
		if (total > 0) {
			return "\"query\":" + queryBuilderValue + ", \"from\" :" + from
					+ ", \"size\" : " + total + "";
		}
		return "\"query\":" + queryBuilderValue + "";
	}
	
	public SearchResponse createNewElasticSearchResult(String json ,String pathToResult) {
		SearchResponse result = new SearchResponse();
		Map jsonMap = convertJsonStringToMapObject(json);
		result.setJsonString(json);
		result.setJsonMap(jsonMap);
		result.setPathToResult(pathToResult);
		result.setSucceeded(true);
		return result;
	}

	public static Map convertJsonStringToMapObject(String jsonTxt) {
		try {
			return GsonContextLoader.getGsonContext().fromJson(jsonTxt,
					Map.class);

		} catch (Exception e) {
			logger.error(" Error while convertJsonStringToMapObject()"+e.getLocalizedMessage(),e); 
		}
		return new HashMap();
	}
	
	private static boolean isCriteria(String criteria) {
		return !(isEmpty(criteria)) ? !excludedtags.contains(criteria) : false;
	}
	
	private static String getQuerydata(String query){
		return "{\n"+query+"\n}";
	}
	public static List<EsSort> getOrderList(String ordr) {
		List<EsSort> sorting = new ArrayList<EsSort>();
		//sorting.add(new EsSort(FieldValue.SCORE.getValue()));
		if (isEmpty(ordr)) {		
			sorting.add(new EsSort("contentId", EsSort.Sorting.DESC));
		} else {
			sorting.add(new EsSort(ordr));
		}
		return sorting;
	}
	public static String generateSearchSortSubQuery(List<EsSort> sortList) {
		StringBuilder sorting = new StringBuilder("");
	//	LOGGER.error("generateSearchSortSubQuery ::sortList  "+sortList);
		if (sortList != null) {
			for (EsSort s : sortList) {
				if (s != sortList.get(0))
					sorting.append(",\n");
				sorting.append(s.toString());
			}
			if (sorting.length() > 0)
				return "\"sort\": [" + sorting.toString() + "], \n";
		}
		return sorting.toString();
	}
}
