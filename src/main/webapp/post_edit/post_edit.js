window.addEventListener('load', () => {
    createForm();
})

function getPostId() {
    return (new URL(window.location.href)).searchParams.get('post');
}

function createForm() {
    const postId = getPostId();

    let form;
    const title = document.createElement('h2');
    if (!postId) {
        title.textContent = 'Create new post';
        form = buildCreateNewPostForm();
    }
    else {
        title.textContent = 'Edit existing post';
        form = buildEditCurrentPostForm();
    }

    const formDiv = document.getElementById('post-form');
    formDiv.appendChild(title);
    formDiv.appendChild(form);
}

function buildCreateNewPostForm() {
    const form = document.createElement('form');

    const descriptionLabel = document.createElement('label');
    const descriptionInput = document.createElement('textarea');
    descriptionInput.id = 'description';
    descriptionInput.name = 'description';
    descriptionInput.className = 'ui-input';
    descriptionLabel.for = descriptionInput.id;
    descriptionLabel.textContent = 'Post content: ';

    const authorsEmailLabel = document.createElement('label');
    const authorsEmailInput = document.createElement('input');
    authorsEmailInput.id = 'authorsEmail';
    authorsEmailInput.name = 'authorsEmail';
    authorsEmailInput.className = 'ui-input';
    authorsEmailLabel.for = authorsEmailInput.id;
    authorsEmailLabel.textContent = 'Post\'s author\'s email: ';

    const button = document.createElement('button');
    button.type = 'button';
    button.value = 'Create';
    button.textContent = 'Create';
    button.onclick = submitNewPost;

    form.appendChild(descriptionLabel);
    form.appendChild(descriptionInput);
    form.appendChild(authorsEmailLabel);
    form.appendChild(authorsEmailInput);
    form.appendChild(button);

    return form;
}

function buildEditCurrentPostForm() {
    const form = document.createElement('form');

    const descriptionLabel = document.createElement('label');
    const descriptionInput = document.createElement('textarea');
    descriptionInput.id = 'description';
    descriptionInput.name = 'description';
    descriptionInput.className = 'ui-input';
    descriptionLabel.for = descriptionInput.id;
    descriptionLabel.textContent = 'Post content: ';

    const button = document.createElement('button');
    button.type = 'button';
    button.value = 'Edit';
    button.textContent = 'Edit';
    button.onclick = submitEdit;

    form.appendChild(descriptionLabel);
    form.appendChild(descriptionInput);
    form.appendChild(button);

    return form;
}

function submitEdit() {
    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 204) {
            const div = document.getElementById('post-form');
            div.innerHTML = '';
            const text = document.createElement('p');
            text.textContent = 'Post was updated!';
            div.appendChild(text);
        }
    };
    xhttp.open('PUT', getContextRoot() + '/api/posts/' + getPostId());
    const request = {
        'description': document.getElementById('description').value
    }
    xhttp.send(JSON.stringify(request));
}

function submitNewPost() {
    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 201) {
            const div = document.getElementById('post-form');
            div.innerHTML = '';
            const text = document.createElement('p');
            text.textContent = 'Post was created!';
            div.appendChild(text);
        }
    };
    xhttp.open('POST', getContextRoot() + '/api/posts/');
    const request = {
        'description': document.getElementById('description').value,
        'authorsEmail': document.getElementById('authorsEmail').value
    }
    xhttp.send(JSON.stringify(request));
}
