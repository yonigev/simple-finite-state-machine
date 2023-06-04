#!/bin/bash

pr_body=$1

if echo "$pr_body" | grep -qE "\[[xX]\] ?[Mm]inor"; then
  echo "minor"
elif echo "$pr_body" | grep -qE "\[[xX]\] ?[Mm]ajor"; then
  echo "major"
else
  echo "patch"
fi