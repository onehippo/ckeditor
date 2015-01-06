CKEditor 4 for Hippo CMS
========================

## Hippo-specific modifications

This repository contains Hippo-specific modifications of CKEditor 4.
The build includes only the plugins used in Hippo CMS (see dev/builder/build-config.js).

### External plugins

The following external plugins are included:

  - [codemirror](https://github.com/w8tcha/CKEditor-CodeMirror-Plugin)
  - [textselection] (https://github.com/w8tcha/CKEditor-TextSelection-Plugin)
  - [wordcount](https://github.com/w8tcha/CKEditor-WordCount-Plugin)
  - [youtube](https://github.com/fonini/ckeditor-youtube-plugin)

## Versions

A Hippo-specific CKEditor build adds a 1-based nano version to the CKEditor version it extends, prefixed with `-h`.
For example, version `4.3.0-h1` extends CKEditor `4.3.0`.

Each branch `hippo/<version>` contains all commits in the CKEditor branch `release/<version>`
plus all Hippo-specific modifications.

A release is available in a tag are named `hippo/<version>`, e.g. `hippo/4.3.0-h1`.

The script `/dev/builder/build.sh` needs a 'build version' parameter, which is burned into the generated code.
Hippo CMS uses the same version number as in the tag name (e.g. '4.3.0-h1').

### Upstream changes 

To get upstream changes, first add a remote for the upstream CKEditor repository: 

    git remote add -f upstream https://github.com/ckeditor/ckeditor-dev.git
    
Upstream changes can now be merged into the current hippo-specific branch. Make sure to merge the upstream branch
with the same version number. For example:
  
    git fetch upstream
    git checkout hippo/4.3.x
    git merge upstream/release/4.3.x
    
When a new major version is released upstream, a new hippo-specific branch should be created based on the upstream 
branch. All hippo-specific customizations in the previous branch can then be merged into the new one. The new 
branch must be pushed to origin, so other people can fetch it too. For example, to upgrade from 4.3.x to 4.4.x:

    git fetch upstream
    git checkout release/4.4.x
    git checkout -b hippo/4.4.x
    git merge hippo/4.3.x
    git push origin hippo/4.4.x
    
### Deployment to Nexus

Prerequisites:

  - [Maven](http://maven.apache.org/)

Deployment command:

    mvn clean deploy
    
## External plugin management

Only a part of each external plugin's code has to be included in the Hippo CKEditor build,
i.e. the part that should to into the CKEditor subdirectory `plugins/XXX`. The history of that
part is kept in a branch `XXX/plugin`. The files are included with `git read-tree` under the 
directory `plugins/XXX`.

For example, say all CodeMirror plugin code is located in a remote branch `codemirror/master`
under the directory `codemirror`. All commits that affect that subdirectory are kept
in the branch `codemirror/plugin`. The code in the branch `codemirror/plugin` is then
included in a Hippo CKEditor branch under the directory `plugins/codemirror`.

### Remotes for existing plugins

Use the following commands to add remotes for the upstream repositories of all external plugins:
 
    git remote add -f upstream-codemirror    https://github.com/w8tcha/CKEditor-CodeMirror-Plugin.git
    git remote add -f upstream-textselection https://github.com/w8tcha/CKEditor-TextSelection-Plugin
    git remote add -f upstream-youtube       https://github.com/fonini/ckeditor-youtube-plugin.git
    git remote add -f upstream-wordcount     https://github.com/w8tcha/CKEditor-WordCount-Plugin.git

### Adding a new external plugin

The following example adds a fictitious external plugin called 'foo' to the Hippo CKEditor 4.3.x build.
Its Git repository contains a subdirectory `code` that should go into the CKEditor directory `plugins/example`.

First add the remote repository of the example plugin to your local Git repository: 

    git remote add -f upstream-foo <remote url>
  
Second, create a local branch for the master branch of the plugin:     
    
    git checkout -b foo/master upstream-foo/master
    
Then create a separate branch 'foo/plugin' for the part of the plugin that should go into the 
'plugins' directory of CKEditor. In our example that's everything in the subdirectory 'code':

    git subtree split --prefix=code/ -b foo/plugin
    
Finally, add the extracted part of the plugin to the 'plugins' directory of CKEditor:
    
    git checkout hippo/4.4.x
    git read-tree --prefix=plugins/foo/ -u foo/plugin

Add the 'foo' plugin to the file `dev/builder/build-config.js` to include it in the Hippo CKEditor build.

### Updating an external plugin

The following example updates the 'foo' plugin with the latest changes from upstream.

If it does not exist yet, create a local branch `foo/master` that tracks the upstream master branch:
 
    git fetch upstream-foo
    git checkout -b foo/master upstream-foo/master
    
Otherwise just pull changes from upstream:

    git checkout foo/master    
    git pull
    
Next, create a branch 'foo/new-plugin' with the new plugin code: 
    
    git subtree split --prefix=code/ -b foo/new-plugin

Now rebase the old plugin code onto the new plugin to avoid merge conflicts:

    git checkout foo/new-plugin
    git rebase foo/plugin
    
Then merge the new plugin code into the plugin branch:

    git checkout foo/plugin
    git merge foo/new-plugin

Now we can include the updated plugin into the CKEditor tree.
First remove the existing plugin from the CKEditor plugins directory:    
    
    git checkout hippo/4.4.x
    rm -rf plugins/foo
    git commit -a -m 'Remove foo plugin version 1'
    
Next, add the updated plugin from the branch again:
     
    git read-tree --prefix=plugins/foo/ -u foo/plugin
    
Push the changes in both the CKEditor branch and the plugin branch,
so it's clear what the history is of the included plugin.

    git checkout hippo/4.4.x
    git push
    git checkout foo/plugin
    git push

Finally, remove the new plugin branch since it is no longer needed:

    git branch -D foo/new-plugin

## The remainder of this file contains the unmodified CKEditor README

## Development Code

This repository contains the development version of CKEditor.

**Attention:** The code in this repository should be used locally and for
development purposes only. We don't recommend distributing it on remote websites
because the user experience will be very limited. For that purpose, you should
build it (see below) or use an official release instead, available on the
[CKEditor website](http://ckeditor.com).

### Code Installation

There is no special installation procedure to install the development code.
Simply clone it on any local directory and you're set.

### Available Branches

This repository contains the following branches:

  - **master**: development of the upcoming minor release.
  - **major**: development of the upcoming major release.
  - **stable**: latest stable release tag point (non-beta).
  - **latest**: latest release tag point (including betas).
  - **release/A.B.x** (e.g. 4.0.x, 4.1.x): release freeze, tests and tagging.
    Hotfixing.

Note that both **master** and **major** are under heavy development. Their
code didn't pass the release testing phase so it may be unstable.

Additionally, all releases will have their relative tags in this form: 4.0,
4.0.1, etc.

### Samples

The `samples/` folder contains a good set of examples that can be used
to test your installation. It can also be a precious resource for learning
some aspects of the CKEditor JavaScript API and its integration on web pages.

### Code Structure

The development code contains the following main elements:

  - Main coding folders:
    - `core/`: the core API of CKEditor. Alone, it does nothing, but
    it provides the entire JavaScript API that makes the magic happen.
    - `plugins/`: contains most of the plugins maintained by the CKEditor core team.
    - `skin/`: contains the official default skin of CKEditor.
    - `dev/`: contains "developer tools".
    - `tests/`: contains CKEditor tests suite.

### Building a Release

A release optimized version of the development code can be easily created
locally. The `dev/builder/build.sh` script can be used for that purpose:

	> ./dev/builder/build.sh

A "release ready" working copy of your development code will be built in the new
`dev/builder/release/` folder. An internet connection is necessary to run the
builder, for its first time at least.

### Testing Environment

Read more on how to set up the environment and execute tests in the [CKEditor Testing Environment](http://docs.ckeditor.com/#!/guide/dev_tests) guide.

### License

Licensed under the GPL, LGPL and MPL licenses, at your choice.

For full details about the license, please check the LICENSE.md file.
