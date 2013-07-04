#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd "$DIR/../"
lein clean
lein uberjar
cp -f target/*standalone.jar ./blueshift.jar