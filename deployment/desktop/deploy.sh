#!/bin/bash

set -e
set -o pipefail

if [[ -z "${ITCH_API_KEY}" ]]; then
  echo "Unable to deploy! No ITCH_API_KEY environment variable specified!"
  exit 1
fi

prepare_butler() {
    echo "Preparing butler..."
    download_if_not_exist http://dl.itch.ovh/butler/linux-amd64/head/butler butler
    chmod +x butler
    touch butler_creds
    echo -n $ITCH_API_KEY > butler_creds
}

prepare_packr() {
    echo "Preparing packr..."
    download_if_not_exist https://libgdx.badlogicgames.com/ci/packr/packr.jar packr.jar
    cp desktop/build/libs/desktop-1.0.jar scape.jar
}

# platform = $1
# packrConfig = $2
# jre = $3
prepare_and_push() {
    echo "Preparing $1 build..."
    download_if_not_exist https://bitbucket.org/alexkasko/openjdk-unofficial-builds/downloads/$3 $3
    java -jar packr.jar --jdk $3 --output scape-$1 -- deployment/desktop/$2

    zip -r scape-$1.zip scape-$1
    rm -rf scape-$1

    echo "Push $1 build to itch.io..."
    ./butler push scape-$1.zip bitbrain/scape:$2 -i butler_creds
}

download_if_not_exist() {
    if [ ! -f $2 ]; then
        curl -L -O $1 > $2
    fi
}

# Initialisation
prepare_butler
prepare_packr

# Deploy Windows
prepare_and_push "windows" "packr-windows.json" "openjdk-1.7.0-u80-unofficial-windows-i586-image.zip"

echo "Done."
exit 0
