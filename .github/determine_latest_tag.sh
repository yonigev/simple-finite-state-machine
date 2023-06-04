#!/bin/bash

git fetch --tags
latest_tag=$(git tag --sort=-v:refname --list "v[0-9]*" | head -n 1)

echo "$latest_tag"
