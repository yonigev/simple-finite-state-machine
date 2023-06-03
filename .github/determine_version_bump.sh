#!/bin/bash
pr_description=$1
echo "$pr_description"
echo "$1"
#if [[ $pr_description =~ (\[x\] ?[Mm]inor) ]]; then
#  echo "minor"
#elif [[ $pr_description =~ (\[x\] ?[Mm]ajor) ]]; then
#  echo "major"
#else
#  echo "patch"
#fi
