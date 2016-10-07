app.factory('errorHandler', function ($uibModal) {
    return function (error) {
        $uibModal.open({
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '../../views/error-modal.html',
            controller: 'ErrorModalInstanceCtrl',
            controllerAs: '$ctrl',
            size: 'lg',
            resolve: {
                error: function () {
                    return error;
                }
            }
        });
    };
});

app.controller('ErrorModalInstanceCtrl', function ($uibModalInstance, error) {
    var $ctrl = this;
    $ctrl.error = error;
    $ctrl.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
});