#!/bin/bash
shopt -s nocasematch  # Enable case-insensitive matching

bump_version() {
    local version=$1
    local part=$2

    # Splitting the version into major, minor, and patch
    IFS='.' read -ra parts <<< "$version"
    major=${parts[0]}
    minor=${parts[1]}
    patch=${parts[2]}
    # Bump the specified part
    case "$part" in
        MAJOR)
            ((major++))
            minor=0
            patch=0
            ;;
        MINOR)
            ((minor++))
            patch=0
            ;;
        PATCH)
            ((patch++))
            ;;
        *)
            echo "Invalid part specified. Usage: bump_version VERSION PART (major|minor|patch)"
            exit 1
            ;;
    esac

    # Constructing the new version
    new_version="$major.$minor.$patch"
    echo "$new_version"
}

# Checking the arguments
if [ $# -ne 2 ]; then
    echo "Usage: $0 VERSION PART (major|minor|patch)"
    exit 1
fi

version="$1"
part="$2"

# Bumping the version
bumped_version=$(bump_version "$version" "$part")
echo "$bumped_version"
