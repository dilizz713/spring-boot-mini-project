let currentPage = 0;
let pageSize = 10;

$(document).ready(function () {

    //handling API errors
    function handleApiError(xhr) {
        const $alert = $('#errorAlert');
        $alert.removeClass('d-none').empty();

        if (xhr.responseJSON) {
            const apiResponse = xhr.responseJSON;
            const msg = $('<strong>').text(apiResponse.message || 'An error occurred.');
            $alert.append(msg);

            if (apiResponse.data && typeof apiResponse.data === 'object') {
                const list = $('<ul class="mt-2 mb-0">');
                for (const field in apiResponse.data) {
                    const item = $('<li>').text(`${field}: ${apiResponse.data[field]}`);
                    list.append(item);
                }
                $alert.append(list);
            }
        } else {
            $alert.text('Unexpected error. Please try again.');
        }
    }

    function clearError() {
        $('#errorAlert').addClass('d-none').empty();
    }


    loadPaginatedJobs();

    // Save
    $('#saveJobBtn').on('click', function () {
        const title = $('#jobTitle').val();
        const companyName = $('#companyName').val();
        const location = $('#jobLocation').val();
        const jobType = $('#jobType').val();
        const description = $('#jobDescription').val();

        $.ajax({
            method: 'POST',
            url: 'http://localhost:8080/api/v1/job/create',
            contentType: 'application/json',
            data: JSON.stringify({
                jobTitle: title,
                company: companyName,
                location: location,
                type: jobType,
                jobDescription: description,
                status: "Activated"
            }),
            success: function (response) {
                clearError();
                alert(response.message || 'Job added successfully!');
                $('#addJobForm')[0].reset();
                $('#addJobModal').modal('hide');
                loadPaginatedJobs();
            },
            error: function (xhr, status, error) {
                console.error('Error saving job:', error);
                handleApiError(xhr);
            }
        });
    });

    // Update Job
    $('#updateJobBtn').on('click', function () {
        const jobId = $('#editJobId').val();
        const status = $('#editJobModal').data('status');
        const updatedJob = {
            id: jobId,
            jobTitle: $('#editJobTitle').val(),
            company: $('#editCompanyName').val(),
            location: $('#editJobLocation').val(),
            type: $('#editJobType').val(),
            jobDescription: $('#editJobDescription').val(),
            status: status
        };

        $.ajax({
            url: 'http://localhost:8080/api/v1/job/update',
            method: 'PUT',
            contentType: 'application/json',
            data: JSON.stringify(updatedJob),
            success: function (response) {
                clearError();
                alert(response.message || 'Job updated successfully!');
                $('#editJobModal').modal('hide');
                loadPaginatedJobs();
            },
            error: function (xhr, status, error) {
                console.error('Error updating job:', error);
                handleApiError(xhr);
            }
        });
    });

    // Delete Job
    $(document).on('click', '.delete-btn', function () {
        const jobId = $(this).data('id');

        if (confirm('Are you sure you want to delete this job?')) {
            $.ajax({
                url: `http://localhost:8080/api/v1/job/delete/${jobId}`,
                method: 'DELETE',
                success: function (response) {
                    clearError();
                    alert(response.message || 'Job deleted successfully!');
                    loadPaginatedJobs();
                },
                error: function (xhr, status, error) {
                    console.error('Error deleting job:', error);
                    handleApiError(xhr);
                }
            });
        }
    });

    // Toggle Job Status (Activate/Deactivate)
    $(document).on('click', '.toggle-status-btn', function () {
        const jobId = $(this).data('id');

        $.ajax({
            url: `http://localhost:8080/api/v1/job/status/${jobId}`,
            method: 'PATCH',
            success: function (response) {
                clearError();
                alert(response.message || 'Job status updated successfully!');
                loadPaginatedJobs();
            },
            error: function (xhr, status, error) {
                console.error('Error updating status:', error);
                handleApiError(xhr);
            }
        });
    });

    // edit button
    $(document).on('click', '.edit-btn', function () {
        const $btn = $(this);
        $('#editJobId').val($btn.data('id'));
        $('#editJobTitle').val($btn.data('title'));
        $('#editCompanyName').val($btn.data('company'));
        $('#editJobLocation').val($btn.data('location'));
        $('#editJobType').val($btn.data('type'));
        $('#editJobDescription').val($btn.data('description'));
        $('#editJobModal').data('status', $btn.data('status'));
        $('#editJobModal').modal('show');
    });

    // Search Jobs
    $('#searchInput').on('input', function () {
        const keyword = $(this).val().trim();

        if (keyword === '') {
            loadPaginatedJobs();
            return;
        }

        $.ajax({
            url: `http://localhost:8080/api/v1/job/search/${encodeURIComponent(keyword)}`,
            method: 'GET',
            dataType: 'json',
            success: function (response) {
                clearError();
                const jobs = response.data;
                const $tbody = $('#jobsTableBody');
                $tbody.empty();

                if (!jobs || jobs.length === 0) {
                    $tbody.append('<tr><td colspan="7" class="text-center">No jobs found</td></tr>');
                    return;
                }

                jobs.forEach((job, index) => {
                    const row = renderJobRow(job, index);
                    $tbody.append(row);
                });
            },
            error: function (xhr, status, error) {
                console.error('Error searching jobs:', error);
                handleApiError(xhr);
            }
        });
    });

    // Load paginated jobs
    function loadPaginatedJobs(page = 0) {
        $.ajax({
            url: `http://localhost:8080/api/v1/job/paginated?page=${page}&size=${pageSize}`,
            method: 'GET',
            dataType: 'json',
            success: function (response) {
                clearError();
                const pageData = response.data;
                const $tbody = $('#jobsTableBody');
                $tbody.empty();

                pageData.content.forEach((job, index) => {
                    const row = renderJobRow(job, page * pageSize + index);
                    $tbody.append(row);
                });

                updatePagination(pageData.totalPages, page);
            },
            error: function (xhr, status, error) {
                console.error('Error loading paginated jobs:', error);
                handleApiError(xhr);
            }
        });
    }


    function updatePagination(totalPages, current) {
        const $pagination = $('#pagination');
        $pagination.empty();

        let html = '';
        if (current > 0) {
            html += `<li class="page-item"><a class="page-link" href="#" data-page="${current - 1}">Previous</a></li>`;
        }

        for (let i = 0; i < totalPages; i++) {
            const displayPageNumber = i * pageSize + 1;
            html += `<li class="page-item ${i === current ? 'active' : ''}">
                        <a class="page-link" href="#" data-page="${i}">${displayPageNumber}</a>
                     </li>`;
        }

        if (current < totalPages - 1) {
            html += `<li class="page-item"><a class="page-link" href="#" data-page="${current + 1}">Next</a></li>`;
        }

        $pagination.html(html);
    }

    // Pagination clicking
    $(document).on('click', '.page-link', function (e) {
        e.preventDefault();
        const page = parseInt($(this).data('page'));
        if (!isNaN(page)) {
            currentPage = page;
            loadPaginatedJobs(page);
        }
    });

    function renderJobRow(job, index) {
        return `
        <tr>
            <td>${index + 1}</td>
            <td>${job.jobTitle}</td>
            <td>${job.company}</td>
            <td>${job.location}</td>
            <td>${job.type}</td>
            <td>
                <span class="badge ${job.status === 'Activated' ? 'bg-success' : 'bg-secondary'}">
                    ${job.status}
                </span>
            </td>
            <td>
                <button class="btn btn-sm btn-warning edit-btn"
                    data-id="${job.id}"
                    data-title="${job.jobTitle}"
                    data-company="${job.company}"
                    data-location="${job.location}"
                    data-type="${job.type}"
                    data-description="${job.jobDescription}"
                    data-status="${job.status}">
                    <i class="bi bi-pencil-square"></i>
                </button>
                <button class="btn btn-sm btn-danger delete-btn" data-id="${job.id}">
                    <i class="bi bi-trash"></i>
                </button>
                <button class="btn btn-sm btn-outline-info toggle-status-btn" data-id="${job.id}">
                    ${job.status === 'Activated' ? 'Deactivate' : 'Activate'}
                </button>
            </td>
        </tr>`;
    }
});
