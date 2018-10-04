'use strict';

/* Controllers */
var dateTimeFormat = "DD.MM.YYYY HH:mm";
var dateFormat = "DD.MM.YYYY";

function formatAsDate(date) {
    if (date) {
        return moment(date).format(dateFormat);
    }
}

myApp.controller('ChangePasswordController', ['$scope', 'AuthService', 'UserPasswordService', '$rootScope', function ($scope, AuthService, UserPasswordService, $rootScope) {
    $scope.error = null;
    $scope.save = function (oldPassword, newPassword) {
        UserPasswordService.changePassword(oldPassword, newPassword, function (result) {
            $scope.error = null;
            if (result && result.id) {
                AuthService.setCurrentUser(result);
                $rootScope.notifier.notify('information', 'Your password has been updated');
            }
        }, function (result) {
            $scope.error = true;
        });
    }
}]);

myApp.controller('UserSettingsController', ['$scope', 'AuthService', 'UserService', '$rootScope', function ($scope, AuthService, UserService, $rootScope) {
    $scope.expectedCalories = AuthService.currentUser().expectedCalories;
    $scope.update = function () {
        $scope.error = null;
        $scope.warning = null;
        var user = AuthService.currentUser();
        user.expectedCalories = $scope.expectedCalories;

        UserService.save(AuthService.currentUser(), function (result) {
            AuthService.setCurrentUser(result);
            $rootScope.notifier.notify('information', 'Your settings have been updated');
        }, function (result) {
            if (result.status == 409) {
                $scope.warning = true;
                AuthService.setCurrentUser(result.data);
                $scope.expectedCalories = result.data.expectedCalories;
            } else {
                $scope.error = true;
            }
        });
    }
}]);

myApp.controller('UserRegisterController', ['$scope', 'UserRegisterService', 'AuthService', function ($scope, UserRegisterService, AuthService) {
    $scope.passwordInput = null;
    $scope.register = function (email, password) {
        $scope.error = null;
        UserRegisterService.register({
            username: email,
            password: password
        }, function (data) {
            AuthService.login(email, password, function () {
                $('#registerModal').modal('hide');
            });
        }, function (error) {
            $scope.error = true;
        });
    };
}]);

myApp.controller('UserLoginController', ['$scope', 'UserRegisterService', 'AuthService', function ($scope, UserRegisterService, AuthService) {
    $scope.login = function (username, password) {
        $scope.error = null;
        AuthService.login(username, password, function () {
            $('#loginModal').modal('hide');
        }, function () {
            $scope.error = true;
        });
    };

}]);


myApp.controller('MealsController', ['$scope', 'MealService', '$rootScope', 'UserService', 'AuthService', function ($scope, MealService, $rootScope, UserService, AuthService) {

    $scope.timeFrom = '00:00';
    $scope.timeTo = '23:59';

    MealService.query({mealId: "all"}, function (result) {
        $scope.meals = result;
    });

    $scope.edit = function (meal) {
        $scope.error = null;
        $scope.warning = null;
        $scope.deleted = null;
        $scope.currentMeal = meal;

        if ($scope.currentMeal.id) {
            var formatter = moment($scope.currentMeal.time, dateTimeFormat);
            $scope.currentMeal.datePart = formatter.format('DD.MM.YYYY');
            $scope.currentMeal.timePart = formatter.format('HH:mm');
        }

        $('#mealModal').modal('show')
    };


    function caloriesForTodayUpdated() {
        var today = new Date();
        MealService.find({
            'date-from': formatAsDate(today),
            'date-to': formatAsDate(today)
        }, function (result) {
            UserService.get({}, function (user) {
                $scope.caloriesForToday = result.reduce(function (a, b) {
                    return a + b.calories;
                }, 0);
                AuthService.setCurrentUser(user);
            })
        })
    }

    $scope.$watch('meals', function () {
        caloriesForTodayUpdated();
    });


    function mealsUpdated() {
        MealService.find({
            'date-from': formatAsDate($scope.dateFrom),
            'date-to': formatAsDate($scope.dateTo),
            'time-from': $scope.timeFrom,
            'time-to': $scope.timeTo
        }, function (result) {
            $scope.meals = result;
        });

    }

    $scope.$watch('dateFrom', mealsUpdated, true);
    $scope.$watch('dateTo', mealsUpdated, true);
    $scope.$watch('timeFrom', mealsUpdated, true);
    $scope.$watch('timeTo', mealsUpdated, true);

    $scope.delete = function (meal) {
        if (window.confirm("Are you sure ?")) {
            MealService.delete({mealId:meal.id,version:meal.version}, function () {
                mealsUpdated();
                $rootScope.notifier.notify('information', 'Meal has been deleted');
            }, function (result) {
                if (result.status == 409) {
                    if (result.data.id) {
                        mealsUpdated();
                        $rootScope.notifier.notify('warning', 'Meal has been modified in the meantime. The delete has not been executed, please resubmit.');
                    } else {
                        $rootScope.notifier.notify('information', 'Meal is already deleted');
                    }

                } else {
                    $rootScope.notifier.notify('error', 'There has been an error while deleting the meal');
                }
            });
        }
    };

    $scope.save = function () {
        var meal = $scope.currentMeal;
        $scope.warning = null;
        $scope.deleted = null;
        $scope.error = null;

        if (meal.datePart instanceof Date) {
            meal.datePart = moment(meal.datePart).format(dateFormat);
        }

        meal.time = meal.datePart + ' ' + meal.timePart;

        MealService.save(meal, function () {
            $('#mealModal').modal('hide');
            $rootScope.notifier.notify('information', 'Meal has been saved.');
            mealsUpdated();
        }, function (result) {
            //conflict
            if (result.status == 409) {
                mealsUpdated();
                if (result.data.id) {
                    $scope.edit(result.data);
                    $scope.warning = true;
                } else {
                    delete $scope.currentMeal.id;
                    delete $scope.currentMeal.version;
                    $scope.deleted = true;
                }
            } else {
                $scope.error = true;
            }
        })
    };
}]);
