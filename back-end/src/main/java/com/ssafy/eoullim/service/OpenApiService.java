package com.ssafy.eoullim.service;

import java.net.URL;

public interface OpenApiService {
  Boolean isValidSchool(String keyword);

  URL getOpenApiUrl(String keyword);
}
