#!/bin/bash

#pr_description=$(curl -sSL $GITHUB_API_URL/repos/$GITHUB_REPOSITORY/pulls/$PR_NUMBER | jq -r '.body')
pr_description="[x] major"

if [[ $pr_description =~ (\[x\] ?[Mm]inor) ]]; then
  echo "minor"
elif [[ $pr_description =~ (\[x\] ?[Mm]ajor) ]]; then
  echo "major"
else
  echo "patch"
fi
