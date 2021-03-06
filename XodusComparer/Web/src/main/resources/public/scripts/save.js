app.controller('SaveController', function ($scope, $http, $location) {

    if (!app.status.inited) {
        $location.path('/setup');
        return;
    }

    $scope.submit = function () {
        if ($scope.form.$valid) {
            $scope.loading = true;
            $http
                    .post('/api/v1/utils/save', $scope.data)
                    .success(function (data) {
                        $scope.loading = false;
                        $location.path('/tables');
                    })
                    .error(function (error) {
                        $scope.loading = false;
                        if (error.validationErrors) {
                            $scope.validationErrors = error.validationErrors;
                        } else {
                            errorHandler(error);
                        }
                    });
        }
    };
});