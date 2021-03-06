#!/usr/bin/groovy
package com.travelstart.plugins.utils

class RestClient {
    String hostname
    String token

    def get(final String endpoint, final Map<String, String> params = [:]) {
        return setupConnection(hostname + endpoint + setupUrlParams(params))
    }

    def post(final String endpoint, final Map<String, String> params = [:], final String body) {
        def connection = setupConnection(hostname + endpoint + setupUrlParams(params))
        connection.setRequestMethod("POST")
        connection.doOutput = true

        def writer = new OutputStreamWriter(connection.outputStream)
        writer.write(body)
        writer.flush()
        writer.close()

        return connection
    }

    static String setupUrlParams(final Map<String, String> params) {
        if (params) {
            return "?" + params.collect { k,v -> "$k=$v" }.join('&')
        }
        else {
            return ""
        }
    }

    HttpURLConnection setupConnection(final String url) {
        final def httpConnection = new URL(url).openConnection() as HttpURLConnection

        if (token) {
            final def auth = token.split(":")
            final def encoded = "${auth[0]}:${auth.length > 1 ? auth[1] : ""}".bytes.encodeBase64().toString()
            httpConnection.setRequestProperty("Authorization", "Basic ${encoded}")
        }

        httpConnection.setRequestProperty("Content-Type", "application/json")

        return httpConnection
    }

}
