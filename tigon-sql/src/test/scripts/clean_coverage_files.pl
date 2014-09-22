#! /usr/bin/perl

# ------------------------------------------------
#   Copyright 2014 AT&T Intellectual Property
#   Licensed under the Apache License, Version 2.0 (the "License");
#   you may not use this file except in compliance with the License.
#   You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
#   Unless required by applicable law or agreed to in writing, software
#   distributed under the License is distributed on an "AS IS" BASIS,
#   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#   See the License for the specific language governing permissions and
#   limitations under the License.
# -------------------------------------------

use Cwd;

$curr_path = getcwd();
if($curr_path =~ /^(.*\/STREAMING)\//){
        $prefix = $1;
}else{
        print "didn't find prefix.\n";
        exit(1);
}

$glob_str = "$prefix/src/ftacmp/*.gcda";
@gcda_files = sort {$a cmp $b} glob($glob_str);

unlink @gcda_files;

