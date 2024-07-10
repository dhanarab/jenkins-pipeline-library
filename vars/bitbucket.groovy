import groovy.json.JsonOutput

def setBuildStatus(credentials, commitHref, state, key, name, description = "", url = "") {  // state: SUCCESSFUL, INPROGRESS, FAILED
  return httpRequest(
    url : "${commitHref}/statuses/build",
    httpMode: "POST",
    requestBody: """{"state":"${state}", "key":"${key}", "name":"${name}", "description":"${description}", "url":"${url}"}""",
    contentType: "APPLICATION_JSON",
    authentication: credentials,
    validResponseCodes: '200:201'
  )
}

def createPullRequestComment(credentials, pullRequestHref, contentRaw) {
  def request = ['content': [ 'raw': contentRaw ]]
  return httpRequest(
    url : "${pullRequestHref}/comments",
    httpMode: "POST",
    requestBody: JsonOutput.toJson(request),
    contentType: "APPLICATION_JSON",
    authentication: credentials,
    validResponseCodes: '200:201'
  )
}
