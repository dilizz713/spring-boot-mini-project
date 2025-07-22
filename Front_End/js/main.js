
$(document).ready(function () {
    loadJobs();

    // save
    $('#saveJobBtn').on('click', function () {
        var title = $('#jobTitle').val();
        var companyName = $('#companyName').val();
        var location = $('#jobLocation').val();
        var jobType = $('#jobType').val();
        var description = $('#jobDescription').val();

        $.ajax({
            method: 'POST',
            url: 'http://localhost:8080/api/v1/job/create',
            contentType: 'application/json',
            data: JSON.stringify({
                jobTitle:title,
                company:companyName,
                location:location,
                type:jobType,
                jobDescription:description,
                status: "Activated"


            }),
            success: function (response) {
                alert('Job added successfully!');
                $('#addJobForm')[0].reset();
                $('#addJobModal').modal('hide');
                loadJobs();
            },
            error: function (xhr, status, error) {
                console.error('Error saving job:', error);
                alert('Failed to add job. Please try again.');
            }
        });
    });

//get all
    function loadJobs(){
        $.ajax({
            url:'http://localhost:8080/api/v1/job/getall',
            method:'GET',
            dataType:'json',
            success:function(jobs){
                const $tbody = $('#jobsTableBody');
                $tbody.empty();

                jobs.forEach((job , index) => {
                   const row = `
                        <tr>
                            <td>${index+1}</td>
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
                        </tr>    
                   `
                    $tbody.append(row);
                });
            },
            error: function (xhr, status, error) {
                console.error('Error load job:', error);
                alert('Failed to load data. Please try again.');
            }


        });


    }

    //  Job Status (Activate/Deactivate)
    $(document).on('click', '.toggle-status-btn', function () {
        const jobId = $(this).data('id');

        $.ajax({
            url: `http://localhost:8080/api/v1/job/status/${jobId}`,
            method: 'PATCH',
            success: function () {
                alert('Job status updated successfully!');
                loadJobs();
            },
            error: function (xhr, status, error) {
                console.error('Error updating status:', error);
                alert('Failed to update job status. Please try again.');
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

    // update
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
            url: `http://localhost:8080/api/v1/job/update`,
            method: 'PUT',
            contentType: 'application/json',
            data: JSON.stringify(updatedJob),
            success: function () {
                alert('Job updated successfully!');
                $('#editJobModal').modal('hide');
                loadJobs();
            },
            error: function () {
                alert('Failed to update job.');
            }
        });
    });

    //delete
    $(document).on('click', '.delete-btn', function () {
        const jobId = $(this).data('id');

        if (confirm('Are you sure you want to delete this job?')) {
            $.ajax({
                url: `http://localhost:8080/api/v1/job/delete/${jobId}`,
                method: 'DELETE',
                success:function (){
                    alert('Job deleted successfully!');
                    loadJobs();
                },
                error: function () {
                    alert('Failed to delete job.');
                }
            })
        }
    });


    $('#searchInput').on('input', function () {
        const keyword = $(this).val().trim();

        if (keyword === '') {
            loadJobs();
            return;
        }

        $.ajax({
            url: `http://localhost:8080/api/v1/job/search/${encodeURIComponent(keyword)}`,
            method: 'GET',
            dataType: 'json',
            success: function (jobs) {
                const $tbody = $('#jobsTableBody');
                $tbody.empty();

                if (!jobs || jobs.length === 0) {
                    $tbody.append('<tr><td colspan="7" class="text-center">No jobs found</td></tr>');
                    return;
                }

                jobs.forEach((job, index) => {
                    const row = `
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
            </tr>
          `;
                    $tbody.append(row);
                });
            },
            error: function () {
                alert('Failed to search jobs. Please try again.');
            }
        });
    });

})


