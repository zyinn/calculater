// Created 2016/06/30
// Updated 2016/09/14
// ReSharper disable NativeTypePrototypeExtending
(function () {
    
    Array.prototype.indexOfItem = function (exp) {
        
        var targetIndex = -1;
        
        if (typeof exp === "function") {
            this.forEach(function (item, index) {
                if (exp(item) && targetIndex < 0) targetIndex = index;
            });
        } else if (typeof exp === "object") {
            this.forEach(function (item, index) {
                if (typeof item.equals !== "function") debugger;

                if (item.equals(exp) && targetIndex < 0) targetIndex = index;
            });
        }
        
        return targetIndex;
    };
    
    Array.prototype.lastIndexOfItem = function (exp) {
        
        var targetIndex = -1;
        
        if (typeof exp === "function") {
            this.forEach(function (item, index) {
                if (exp(item)) targetIndex = index;
            });
        } else if (typeof exp === "object") {
            this.forEach(function (item, index) {
                if (item.equals(exp)) targetIndex = index;
            });
        }
        
        return targetIndex;
    };
    
    Array.prototype.containsItem = function (exp) {

        var index = this.indexOfItem(exp);  

        return index !== undefined && index !== -1;
    };
     
    Array.prototype.findItem = function (exp) {
        if (!(typeof exp === "function")) return undefined;
        
        var target = undefined;
        
        this.forEach(function (item, index) {
            if (exp(item) && !target) target = item;
        });
        
        return target;
    };
    
    Array.prototype.findWhere = function (exp) {
        if (!(typeof exp === "function")) return [];
        
        var result = [];
        
        this.forEach(function (item, index) {
            if (exp(item)) result.push(item);
        });
        
        return result;
    };
    
    Array.prototype.equals = function (arr) {
        if (!arr || !(arr instanceof Array) || arr.length !== this.length) return false;
        
        for (var i = 0; i < this.length; i++) {
            if (this[i] !== arr[i]) return false;
        }
        
        return true;
    };

    Array.prototype.ifAll = function (exp) {
        
        var result = this.findWhere(exp);

        return result.length > 0 && result === this.length;
    };

    Array.prototype.ifAny = function (exp) {
        
        var result = this.findWhere(exp);
        
        return result.length > 0;
    };

})();