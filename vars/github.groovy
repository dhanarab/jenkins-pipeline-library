def createRelease(credentials, owner, repo, tag) {
  httpRequest(
    url : "https://api.github.com/repos/${owner}/${repo}/releases",
    httpMode: "POST",
    requestBody: """{"tag_name":"${tag}"}""",
    contentType: "APPLICATION_JSON",
    authentication: credentials,
    validResponseCodes: '200:201'
  )
}
