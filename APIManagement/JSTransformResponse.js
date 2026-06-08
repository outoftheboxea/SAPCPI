var responseContent = context.getVariable("response.content");
var posts = JSON.parse(responseContent);

if (Array.isArray(posts)) {
    posts.forEach(function(post) {
        delete post.userId;
        post.postTitle = post.title;
        delete post.title;
        post.content = post.body;
        delete post.body;
        post.fetchedAt = new Date().toISOString();
        post.source = "SAP API Management";
        post.apiVersion = "v1";
        if (post.content) {
            post.preview = post.content.substring(0, 100) + "...";
        }
    });
}

context.setVariable("response.content", JSON.stringify(posts));
