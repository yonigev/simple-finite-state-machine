#!/bin/bash

pr_body=$1

if echo "$pr_body" | grep -qE "\[[xXvV]\] ?[Mm]inor"; then
  echo "minor"
elif echo "$pr_body" | grep -qE "\[[xXvV]\] ?[Mm]ajor"; then
  echo "major"
elif echo "$pr_body" | grep -qE "\[[xXvV]\] ?[Pp]atch"; then
  echo "patch"
else
  echo ""
fi