def getRepositoryUrl() {
    return scm.getUserRemoteConfigs()[0].getUrl()
}

def getRepositoryName() {
    return sh(returnStdout: true, script: """
        basename \$(git remote get-url origin) | sed -e "s/.git\$//"
        """).trim()
}

def getBranchName() {
    value = sh(returnStdout: true, script: """
        git branch --show-current
        """).trim()
    if (value) {
      return value
    }
    else {
      return null
    }
}


def getCommitHash() { 
    return sh(returnStdout: true, script: "git rev-parse HEAD").trim()
}

def getCommitHashShort() { 
    return  sh(returnStdout: true, script: 'git rev-parse --short=10 HEAD').trim()
}

def getCommitMessage() {
    return sh(returnStdout: true, script: 'git log -1 --pretty=%B').trim()
}

def checkout(gitCredentials, gitUrl, branch = 'master', init = false, submoduleInitUpdate = false) {
    sshagent(credentials: [gitCredentials]) {
        if (isUnix()) {
            if (init) {
                 sh """
                    bash -c "[ -f ~/.ssh/known_hosts ] && ssh-keygen -R bitbucket.org" 
                    bash -c "mkdir -p ~/.ssh && ssh-keyscan -H bitbucket.org >> ~/.ssh/known_hosts"
                    if ! git rev-parse --is-inside-work-tree 2>&1 >/dev/null; then

                        git init .
                    fi
                    if ! git remote add origin ${gitUrl} 2>&1 >/dev/null; then
                         git remote set-url origin ${gitUrl}
                    fi
                """
            }
            sh """
                git -c credential.helper= fetch origin
                git -c credential.helper= checkout -f origin/${branch}
                git -c credential.helper= checkout -B ${branch}
            """
            if (submoduleInitUpdate) {
                sh """
                git -c credential.helper= submodule update --init -f
                """
            }
        }
        else {
            if (init) {
                 bat """
                    bash -c "[ -f ~/.ssh/known_hosts ] && ssh-keygen -R bitbucket.org" 
                    bash -c "mkdir -p ~/.ssh && ssh-keyscan -H bitbucket.org >> ~/.ssh/known_hosts"
                    git rev-parse --is-inside-work-tree > NUL 2>&1
                    if %ERRORLEVEL% NEQ 0 (
                        git init .
                    )
                    git remote add origin ${gitUrl} > NUL 2>&1
                    if %ERRORLEVEL% NEQ 0 (
                        git remote set-url origin ${gitUrl}
                    )
                """
            }
            bat """
                git -c credential.helper= fetch origin
                git -c credential.helper= checkout -f origin/${branch}
                git -c credential.helper= checkout -B ${branch}
            """
            if (submoduleInitUpdate) {
                bat """
                    git -c credential.helper= submodule update --init -f
                """
            }
        }
    }
}