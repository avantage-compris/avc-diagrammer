#!/bin/sh

# Generate SVG and PNG images for the documentation.
#

mvn clean test

cp target/*.svg src/site/resources/images/

mvn pre-site

cp target/site/images/*.png src/site/resources/rasterized/
