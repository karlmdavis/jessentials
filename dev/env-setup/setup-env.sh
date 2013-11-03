#!/bin/sh

# This script will:
# 1. Download the required Puppet modules into a temporary directory.
# 2. Launch Puppet and use it to apply the site.pp manifest in this folder.
#
# Prerequisites:
# * Puppet 3.3 or later
# * librarian-puppet
# * Git
#
# References:
# * https://github.com/rodjek/librarian-puppet
# * http://bundler.io/
# * http://docs.puppetlabs.com/windows/from_source.html
# * http://crohr.me/journal/2012/who-said-puppet-was-hard.html
# * https://github.com/sheerun/puppet-solo/blob/master/bin/run

# Install the required Puppet modules into a temporary directory here.
librarian-puppet clean
librarian-puppet install

# Apply the manifest with Puppet.
puppet apply site.pp --modulepath modules/

