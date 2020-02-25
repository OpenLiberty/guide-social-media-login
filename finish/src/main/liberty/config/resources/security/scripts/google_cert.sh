#!/bin/bash

keytool \
    -printcert -rfc \
    -sslserver accounts.google.com:443 \
    > google.pem
keytool \
    -printcert -rfc \
    -sslserver www.googleapis.com:443 \
    > googleapis.pem

keytool \
    -import -trustcacerts \
    -file google.pem
    -alias google
    -keystore slts.p12
    -storetype PKCS12
    -storepass changeit
keytool \
    -import -trustcacerts \
    -file googleapis.pem
    -alias googleapis
    -keystore slts.p12
    -storetype PKCS12
    -storepass changeit