# May 8, 2020
Linked issue: [Archive download/cache frontend action](https://github.com/xuganyu96/PyArchive/issues/9)

## Overview
On each archive's detila page, offer a button that downloads the archive file if the archive is locally cached, or that queues download jobs if the archive is not locally cached.

## Key points
**Implementing the download action** is surprisingly easy given that I have set up `Archive.archive_file` to be a `django.models.FileField`, which automatically grants it an URL. As a result, implementing the download action is one line of code in `templates/archive/archive_detail.html`:
```
<a class="btn btn-outline-info btn-sm mt-1 mb-1" href="{{ object.archive_file.url }}">Download</a>
```
where `object` is an `Archive` object.

**Implementing the cache action** is a little bit more complicated, mostly because I used a class based view for the detail view. Since the `cache` button will be a `submit` button, I need to overwrite the `DetailView` class's default methods for handling `POST` requests. This is accomplished by overwriting the `post` instance method in the child class:
```python
class ArchiveDetailView(LoginRequiredMixin, UserPassesTestMixin, DetailView):
    model = Archive

    #   Overwrite the post method
    def post(self, request, *args, **kwargs):
        archive = self.get_object()
        #   If a POST method is called then it must be 'cache' request
        if 'cache_archive' in request.POST:
            queue_archive_caching(archive)
            messages.success(request, 'Caching jobs queued for this archive')
            return redirect(reverse('archive-detail', kwargs={'pk': archive.archive_id}))
        else:
            messages.warning(request, 'invalid action!')
            return redirect(reverse('archive-detail', kwargs={'pk': archive.archive_id}))
```
Where `queue_archive_caching` is in `archive/utils.py` and is responsible for creating the download jobs and saving them to `PersistentTransferJob`.
