package com.example.zeero.constants;

public class ApiPaths {

    private ApiPaths() {

    }

    public static final String BASE_API = "/api";

    public static final class ToDo {
        public static final String ROOT = BASE_API + "/todo";
        public static final String GET_BY_ID = ROOT + "/{id}";
        public static final String UPDATE = ROOT + "/{id}";
        public static final String TEMPORAL_DELETE = ROOT + "/{id}";
        public static final String PERMANENT_DELETE = ROOT + "/hard-delete/{id}";

    }
}
