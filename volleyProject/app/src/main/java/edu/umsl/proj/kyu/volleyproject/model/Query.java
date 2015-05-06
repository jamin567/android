package edu.umsl.proj.kyu.volleyproject.model;


import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Query {
	private List<Page> pages = new ArrayList<>();

	public List<Page> getPages() {
		return pages;
	}

	public static class GsonDeserializer implements JsonDeserializer<Query> {
		@Override
		public Query deserialize(JsonElement json, Type type,  JsonDeserializationContext context) throws JsonParseException {
			Query query = new Query();

			Set<Map.Entry<String, JsonElement>> pages = json.getAsJsonObject().get("query").getAsJsonObject().get("pages").getAsJsonObject().entrySet();

			Gson gson = new Gson();

			for (Map.Entry<String, JsonElement> page : pages) {
				query.pages.add(gson.fromJson(page.getValue(), Page.class));
			}

			return query;
		}
	}
}
