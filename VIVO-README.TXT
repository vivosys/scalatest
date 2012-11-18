1. git svn remote (add to .git/config):

[svn-remote "svn"]
        url = http://scalatest.googlecode.com/svn
        fetch = trunk:refs/remotes/trunk
        branches = branches/*:refs/remotes/*
        tags = tags/*:refs/remotes/tags/*

Upload command:

REPO_ID=vivosys-repo
REPO_URL=https://tools.vivosys.com/artifactory/ext-release-local
mvn -DrepositoryId=$REPO_ID -Durl=$REPO_URL -Dfile=dist/lib/scalatest.jar \
  -DgroupId=org.scalatest -DartifactId=scalatest_2.10.0-RC2 -Dversion=1.8.vivo_1 \
  deploy:deploy-file

