
// Main controller
webMsgs.controller('MainController', ['$scope', 'remoteMsgs', function($scope, remoteMsgs) {
    //-----------------------------------------
    // Data
    $scope.user = null;
    $scope.accounts = null;
    $scope.selectedView = null; // view name

    //-----------------------------------------
    // Operations
    $scope.selectView = function(viewname) {
        $scope.selectedView = viewname;
    };

    //-----------------------------------------
    // Initial load
    remoteMsgs.getUser(function(user) {
        $scope.user = user;
        $scope.selectedView = 'INBOX';
    });

}]);
 

