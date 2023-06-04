#!/bin/bash

pr_body=$1

if [[ $pr_body =~ (\[x\] ?[Mm]inor) ]]; then
  echo "minor"
elif [[ $pr_body =~ (\[x\] ?[Mm]ajor) ]]; then
  echo "major"
else
  echo "patch"
fi