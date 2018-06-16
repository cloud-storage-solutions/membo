#!/usr/bin/bash

hex="${1}"
curl -X POST "https://rest.bitbox.earth/v1/rawtransactions/sendRawTransaction/${hex}" -H "accept: application/json"

