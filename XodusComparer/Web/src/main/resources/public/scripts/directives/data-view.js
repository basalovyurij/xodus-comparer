app.directive('dataView', [function () {
    return {
        restrict: 'E',
        scope: {
            selectedType: '&'
        },
        replace: true,
        template: require('../../views/data-view.html')
    };
}]);