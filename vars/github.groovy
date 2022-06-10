import groovy.json.JsonOutput

def createRelease(credentials, owner, repo, tag_name, name = null, body = null) {
  def request = ['tag_name': tag_name]
  if (name) {
    request['name'] = name
  }
  if (body) {
    request['body'] = body
  }
  httpRequest(
    url : "https://api.github.com/repos/${owner}/${repo}/releases",
    httpMode: 'POST',
    requestBody: JsonOutput.toJson(request),
    contentType: 'APPLICATION_JSON',
    authentication: credentials,
    validResponseCodes: '200:201'
  )
}
