/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package suhockii.dev.weather.api;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Map;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import okio.BufferedSource;
import okio.Okio;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import suhockii.dev.weather.data.models.places.LatLng;
import suhockii.dev.weather.data.net.PlacesApi;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class PlacesApiTest {
    private PlacesApi placesApi;
    private MockWebServer mockWebServer;

    @Before
    public void createService() throws IOException {
        mockWebServer = new MockWebServer();
        placesApi = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(mockWebServer.url("/"))
                .build()
                .create(PlacesApi.class);
    }

    @After
    public void stopService() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    public void getPlaces() throws IOException, InterruptedException {
        enqueueResponse("places-autocomplete.json");
        String query = "Мос";
        String apiKey = "apiKey";
        placesApi.getPlaces(query, apiKey).subscribe(placesResponse -> {
            assertThat(placesResponse, notNullValue());
            assertTrue(placesResponse.isSuccess());
            assertThat(placesResponse.getPlaceNameAt(0), is("Москва, Россия"));
            assertThat(placesResponse.getPlaceIdAt(0), is("ChIJybDUc_xKtUYRTM9XV8zWRD0"));
            assertThat(placesResponse.getPlaceNameAt(1), is("Московская область, Россия"));
            assertThat(placesResponse.getPlaceIdAt(1), is("ChIJ2cXDsDGySkERQvLeO8CzDJE"));
        });
        RecordedRequest request = mockWebServer.takeRequest();
        String urlEncodedQuery = URLEncoder.encode(query, "UTF-8");
        assertThat(request.getPath(), is("/autocomplete/json?input=" + urlEncodedQuery + "&key=" + apiKey));
    }

    @Test
    public void getPlaceDetails() throws IOException, InterruptedException {
        enqueueResponse("places-details.json");
        String placeId = "ChIJybDUc_xKtUYRTM9XV8zWRD0";
        String apiKey = "apiKey";
        placesApi.getPlaceDetailsById(placeId, apiKey).subscribe(detailsResponse -> {
            assertThat(detailsResponse, notNullValue());
            assertThat(detailsResponse.getCoords(), is(new LatLng(55.755826, 37.6173)));
        });
        RecordedRequest request = mockWebServer.takeRequest();
        assertThat(request.getPath(), is("/details/json?placeid=" + placeId + "&key=" + apiKey));
    }

    private void enqueueResponse(String fileName) throws IOException {
        enqueueResponse(fileName, Collections.emptyMap());
    }

    private void enqueueResponse(String fileName, Map<String, String> headers) throws IOException {
        InputStream inputStream = getClass().getClassLoader()
                .getResourceAsStream("api-response/" + fileName);
        BufferedSource source = Okio.buffer(Okio.source(inputStream));
        MockResponse mockResponse = new MockResponse();
        for (Map.Entry<String, String> header : headers.entrySet()) {
            mockResponse.addHeader(header.getKey(), header.getValue());
        }
        mockWebServer.enqueue(mockResponse
                .setBody(source.readString(Charset.forName("UTF-8"))));
    }

}
