app.directive('dataview', function () {
    return {
        restrict: 'E',
        scope: {
            selectedType: '&'
        },
        templateUrl: '../../views/data-view.html'
    };
});