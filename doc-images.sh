#!/bin/sh

# Generate SVG and PNG images for the documentation.
#

######## 1. GENERATE SVG IMAGES FROM JAVA SAMPLES

mvn clean test

######## 2. COPY SVG IMAGES TO src/site/

cp target/*.svg src/site/resources/images/

######## 3. RASTERIZE SVG IMAGES INTO PNG

mvn pre-site

######## 4. COPY PNG IMAGES TO src/site/

cp target/site/images/*.png src/site/resources/rasterized/
