window.addEventListener('load', () => {
    loadPosts();
})

function createAddPostButton() {
    const button = document.createElement('button');
    button.textContent = 'Create new post';
    button.onclick = function () {
        window.location.href = getContextRoot() + '/post_edit/post_edit.html';
    };

    return button;
}

function clearPage() {
    document.getElementById('post-list').innerHTML = '';
    document.getElementById('post-list').appendChild(createAddPostButton());
}

function createPost(post) {
    const div = document.createElement('div');
    div.id = post.id;
    div.className = 'post-container';
    const authorName = document.createElement('h2');
    authorName.textContent = 'Author';
    authorName.id = post.id + '_author_name';
    const authorLink = document.createElement('a');
    authorLink.appendChild(authorName);
    authorLink.href = getContextRoot() + '/api/users/' + post.authorsEmail;
    const date = document.createElement('p');
    date.textContent = post.creationTime;
    const description = document.createElement('p');
    description.textContent = post.description;
    const commentsLink = document.createElement('a');
    commentsLink.textContent = 'Comments';
    commentsLink.href = '';
    const editButton = document.createElement('button');
    editButton.textContent = 'Edit';
    editButton.onclick = function () {
        window.location.href = getContextRoot() + '/post_edit/post_edit.html?post=' + post.id
    }
    const deleteButton = document.createElement('button');
    deleteButton.textContent = 'Delete';
    deleteButton.onclick = function () {

    }
    const commentButton = document.createElement('button');
    commentButton.textContent = 'Leave a comment';
    commentButton.onclick = function () {

    }

    div.appendChild(authorLink);
    div.appendChild(editButton);
    div.appendChild(deleteButton);
    div.appendChild(date);
    div.appendChild(description);
    div.appendChild(commentsLink);
    div.appendChild(commentButton);

    return div;
}

function loadPost(id) {
    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200) {
            let response = JSON.parse(this.responseText);
            let tbody = document.getElementById('post-list');
            tbody.appendChild(createPost(response));
        }
    }
    xhttp.open("GET", getContextRoot() + '/api/posts/' + id, true);
    xhttp.send();
}

function loadPosts() {
    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200) {
            let response = JSON.parse(this.responseText);
            let tbody = document.getElementById('post-list');
            clearPage();
            response.posts.forEach(post => {
                loadPost(post.id);
            });
        }
    };
    xhttp.open("GET", getContextRoot() + '/api/posts', true);
    xhttp.send();
}