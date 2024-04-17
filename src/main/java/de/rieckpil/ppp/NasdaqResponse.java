package de.rieckpil.ppp;

import java.util.List;

public record NasdaqResponse(
    CountryMetaResponse countryMetaResponse, Meta meta, DataTable datatable) {}

record DataTable(List<List<Object>> data, List<Column> columns) {}

record Column(String name, String type) {}

record Meta(String nextCursorId) {}
