var app = angular.module('todoapp', [
    'ngRoute',
    'ui.bootstrap'
]);

app.config(function ($routeProvider) {
    $routeProvider
            .when('/setup', {
                templateUrl: 'views/setup.html',
                controller: 'SetupController'
            })
            .when('/setup/new', {
                templateUrl: 'views/setup-new.html',
                controller: 'SetupNewController'
            })
            .when('/setup/load', {
                templateUrl: 'views/setup-old.html',
                controller: 'SetupOldController'
            })
            .when('/tables', {
                templateUrl: 'views/tables.html',
                controller: 'TablesCtrl'
            })
            .when('/type/:name', {
                templateUrl: 'views/type.html',
                controller: 'TypeCtrl'
            })
            .otherwise({
                redirectTo: '/setup'
            })
});

app.controller('ListCtrl', function ($scope, $http) {
    $http.get('/api/v1/todos').success(function (data) {
        $scope.todos = data;
    }).error(function (data, status) {
        console.log('Error ' + data)
    })

    $scope.todoStatusChanged = function (todo) {
        console.log(todo);
        $http.put('/api/v1/todos/' + todo.id, todo).success(function (data) {
            console.log('status changed');
        }).error(function (data, status) {
            console.log('Error ' + data)
        })
    }
});

app.controller('CreateCtrl', function ($scope, $http, $location) {
    $scope.todo = {
        done: false
    };

    $scope.createTodo = function () {
        console.log($scope.todo);
        $http.post('/api/v1/todos', $scope.todo).success(function (data) {
            $location.path('/');
        }).error(function (data, status) {
            console.log('Error ' + data)
        })
    }
});