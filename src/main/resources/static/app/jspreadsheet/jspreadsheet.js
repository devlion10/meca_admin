var $jscomp = $jscomp || {};
$jscomp.scope = {};
$jscomp.arrayIteratorImpl = function(p) {
    var q = 0;
    return function() {
        return q < p.length ? {
            done: !1,
            value: p[q++]
        } : {
            done: !0
        }
    }
};
$jscomp.arrayIterator = function(p) {
    return {
        next: $jscomp.arrayIteratorImpl(p)
    }
};
$jscomp.ASSUME_ES5 = !1;
$jscomp.ASSUME_NO_NATIVE_MAP = !1;
$jscomp.ASSUME_NO_NATIVE_SET = !1;
$jscomp.SIMPLE_FROUND_POLYFILL = !1;
$jscomp.ISOLATE_POLYFILLS = !1;
$jscomp.FORCE_POLYFILL_PROMISE = !1;
$jscomp.FORCE_POLYFILL_PROMISE_WHEN_NO_UNHANDLED_REJECTION = !1;
$jscomp.defineProperty = $jscomp.ASSUME_ES5 || "function" == typeof Object.defineProperties ? Object.defineProperty : function(p, q, h) {
    if (p == Array.prototype || p == Object.prototype) return p;
    p[q] = h.value;
    return p
};
$jscomp.getGlobal = function(p) {
    p = ["object" == typeof globalThis && globalThis, p, "object" == typeof window && window, "object" == typeof self && self, "object" == typeof global && global];
    for (var q = 0; q < p.length; ++q) {
        var h = p[q];
        if (h && h.Math == Math) return h
    }
    throw Error("Cannot find global object");
};
$jscomp.global = $jscomp.getGlobal(this);
$jscomp.IS_SYMBOL_NATIVE = "function" === typeof Symbol && "symbol" === typeof Symbol("x");
$jscomp.TRUST_ES6_POLYFILLS = !$jscomp.ISOLATE_POLYFILLS || $jscomp.IS_SYMBOL_NATIVE;
$jscomp.polyfills = {};
$jscomp.propertyToPolyfillSymbol = {};
$jscomp.POLYFILL_PREFIX = "$jscp$";
var $jscomp$lookupPolyfilledValue = function(p, q) {
    var h = $jscomp.propertyToPolyfillSymbol[q];
    if (null == h) return p[q];
    h = p[h];
    return void 0 !== h ? h : p[q]
};
$jscomp.polyfill = function(p, q, h, e) {
    q && ($jscomp.ISOLATE_POLYFILLS ? $jscomp.polyfillIsolated(p, q, h, e) : $jscomp.polyfillUnisolated(p, q, h, e))
};
$jscomp.polyfillUnisolated = function(p, q, h, e) {
    h = $jscomp.global;
    p = p.split(".");
    for (e = 0; e < p.length - 1; e++) {
        var t = p[e];
        if (!(t in h)) return;
        h = h[t]
    }
    p = p[p.length - 1];
    e = h[p];
    q = q(e);
    q != e && null != q && $jscomp.defineProperty(h, p, {
        configurable: !0,
        writable: !0,
        value: q
    })
};
$jscomp.polyfillIsolated = function(p, q, h, e) {
    var t = p.split(".");
    p = 1 === t.length;
    e = t[0];
    e = !p && e in $jscomp.polyfills ? $jscomp.polyfills : $jscomp.global;
    for (var z = 0; z < t.length - 1; z++) {
        var C = t[z];
        if (!(C in e)) return;
        e = e[C]
    }
    t = t[t.length - 1];
    h = $jscomp.IS_SYMBOL_NATIVE && "es6" === h ? e[t] : null;
    q = q(h);
    null != q && (p ? $jscomp.defineProperty($jscomp.polyfills, t, {
        configurable: !0,
        writable: !0,
        value: q
    }) : q !== h && (void 0 === $jscomp.propertyToPolyfillSymbol[t] && (h = 1E9 * Math.random() >>> 0, $jscomp.propertyToPolyfillSymbol[t] = $jscomp.IS_SYMBOL_NATIVE ?
        $jscomp.global.Symbol(t) : $jscomp.POLYFILL_PREFIX + h + "$" + t), $jscomp.defineProperty(e, $jscomp.propertyToPolyfillSymbol[t], {
        configurable: !0,
        writable: !0,
        value: q
    })))
};
$jscomp.initSymbol = function() {};
$jscomp.polyfill("Symbol", function(p) {
    if (p) return p;
    var q = function(z, C) {
        this.$jscomp$symbol$id_ = z;
        $jscomp.defineProperty(this, "description", {
            configurable: !0,
            writable: !0,
            value: C
        })
    };
    q.prototype.toString = function() {
        return this.$jscomp$symbol$id_
    };
    var h = "jscomp_symbol_" + (1E9 * Math.random() >>> 0) + "_",
        e = 0,
        t = function(z) {
            if (this instanceof t) throw new TypeError("Symbol is not a constructor");
            return new q(h + (z || "") + "_" + e++, z)
        };
    return t
}, "es6", "es3");
$jscomp.polyfill("Symbol.iterator", function(p) {
        if (p) return p;
        p = Symbol("Symbol.iterator");
        for (var q = "Array Int8Array Uint8Array Uint8ClampedArray Int16Array Uint16Array Int32Array Uint32Array Float32Array Float64Array".split(" "), h = 0; h < q.length; h++) {
            var e = $jscomp.global[q[h]];
            "function" === typeof e && "function" != typeof e.prototype[p] && $jscomp.defineProperty(e.prototype, p, {
                configurable: !0,
                writable: !0,
                value: function() {
                    return $jscomp.iteratorPrototype($jscomp.arrayIteratorImpl(this))
                }
            })
        }
        return p
    }, "es6",
    "es3");
$jscomp.iteratorPrototype = function(p) {
    p = {
        next: p
    };
    p[Symbol.iterator] = function() {
        return this
    };
    return p
};
$jscomp.iteratorFromArray = function(p, q) {
    p instanceof String && (p += "");
    var h = 0,
        e = !1,
        t = {
            next: function() {
                if (!e && h < p.length) {
                    var z = h++;
                    return {
                        value: q(z, p[z]),
                        done: !1
                    }
                }
                e = !0;
                return {
                    done: !0,
                    value: void 0
                }
            }
        };
    t[Symbol.iterator] = function() {
        return t
    };
    return t
};
$jscomp.polyfill("Array.prototype.keys", function(p) {
    return p ? p : function() {
        return $jscomp.iteratorFromArray(this, function(q) {
            return q
        })
    }
}, "es6", "es3");
(function(p, q) {
    "object" === typeof exports && "undefined" !== typeof module ? module.exports = q() : "function" === typeof define && define.amd ? define(q) : p.formula = q()
})(this, function() {
    var p = function(q) {
        var h = function() {
                var g = {};
                g.nil = Error("#NULL!");
                g.div0 = Error("#DIV/0!");
                g.value = Error("#VALUE!");
                g.ref = Error("#REF!");
                g.name = Error("#NAME?");
                g.num = Error("#NUM!");
                g.na = Error("#N/A");
                g.error = Error("#ERROR!");
                g.data = Error("#GETTING_DATA");
                return g
            }(),
            e = function() {
                var g = {
                        flattenShallow: function(a) {
                            return a && a.reduce ?
                                a.reduce(function(c, d) {
                                    var f = Array.isArray(c),
                                        k = Array.isArray(d);
                                    return f && k ? c.concat(d) : f ? (c.push(d), c) : k ? [c].concat(d) : [c, d]
                                }) : a
                        },
                        isFlat: function(a) {
                            if (!a) return !1;
                            for (var c = 0; c < a.length; ++c)
                                if (Array.isArray(a[c])) return !1;
                            return !0
                        },
                        flatten: function() {
                            for (var a = g.argsToArray.apply(null, arguments); !g.isFlat(a);) a = g.flattenShallow(a);
                            return a
                        },
                        argsToArray: function(a) {
                            var c = [];
                            g.arrayEach(a, function(d) {
                                c.push(d)
                            });
                            return c
                        },
                        numbers: function() {
                            return this.flatten.apply(null, arguments).filter(function(a) {
                                return "number" ===
                                    typeof a
                            })
                        },
                        cleanFloat: function(a) {
                            return Math.round(1E14 * a) / 1E14
                        },
                        parseBool: function(a) {
                            if ("boolean" === typeof a || a instanceof Error) return a;
                            if ("number" === typeof a) return 0 !== a;
                            if ("string" === typeof a) {
                                var c = a.toUpperCase();
                                if ("TRUE" === c) return !0;
                                if ("FALSE" === c) return !1
                            }
                            return a instanceof Date && !isNaN(a) ? !0 : h.value
                        },
                        parseNumber: function(a) {
                            return void 0 === a || "" === a ? h.value : isNaN(a) ? h.value : parseFloat(a)
                        },
                        parseNumberArray: function(a) {
                            var c;
                            if (!a || 0 === (c = a.length)) return h.value;
                            for (var d; c--;) {
                                d =
                                    g.parseNumber(a[c]);
                                if (d === h.value) return d;
                                a[c] = d
                            }
                            return a
                        },
                        parseMatrix: function(a) {
                            if (!a || 0 === a.length) return h.value;
                            for (var c, d = 0; d < a.length; d++)
                                if (c = g.parseNumberArray(a[d]), a[d] = c, c instanceof Error) return c;
                            return a
                        }
                    },
                    b = new Date(Date.UTC(1900, 0, 1));
                g.parseDate = function(a) {
                    if (!isNaN(a)) {
                        if (a instanceof Date) return new Date(a);
                        a = parseInt(a, 10);
                        return 0 > a ? h.num : 60 >= a ? new Date(b.getTime() + 864E5 * (a - 1)) : new Date(b.getTime() + 864E5 * (a - 2))
                    }
                    return "string" !== typeof a || (a = new Date(a), isNaN(a)) ? h.value :
                        a
                };
                g.parseDateArray = function(a) {
                    for (var c = a.length, d; c--;) {
                        d = this.parseDate(a[c]);
                        if (d === h.value) return d;
                        a[c] = d
                    }
                    return a
                };
                g.anyIsError = function() {
                    for (var a = arguments.length; a--;)
                        if (arguments[a] instanceof Error) return !0;
                    return !1
                };
                g.arrayValuesToNumbers = function(a) {
                    for (var c = a.length, d; c--;) d = a[c], "number" !== typeof d && (!0 === d ? a[c] = 1 : !1 === d ? a[c] = 0 : "string" === typeof d && (d = this.parseNumber(d), a[c] = d instanceof Error ? 0 : d));
                    return a
                };
                g.rest = function(a, c) {
                    return a && "function" === typeof a.slice ? a.slice(c ||
                        1) : a
                };
                g.initial = function(a, c) {
                    return a && "function" === typeof a.slice ? a.slice(0, a.length - (c || 1)) : a
                };
                g.arrayEach = function(a, c) {
                    for (var d = -1, f = a.length; ++d < f && !1 !== c(a[d], d, a););
                    return a
                };
                g.transpose = function(a) {
                    return a ? a[0].map(function(c, d) {
                        return a.map(function(f) {
                            return f[d]
                        })
                    }) : h.value
                };
                return g
            }(),
            t = {};
        t.datetime = function() {
            function g(d) {
                return (d - a) / 864E5 + (-22038912E5 < d ? 2 : 1)
            }
            var b = {},
                a = new Date(1900, 0, 1),
                c = [
                    [],
                    [1, 2, 3, 4, 5, 6, 7],
                    [7, 1, 2, 3, 4, 5, 6],
                    [6, 0, 1, 2, 3, 4, 5],
                    [],
                    [],
                    [],
                    [],
                    [],
                    [],
                    [],
                    [7, 1, 2, 3, 4, 5, 6],
                    [6, 7, 1, 2, 3, 4, 5],
                    [5, 6, 7, 1, 2, 3, 4],
                    [4, 5, 6, 7, 1, 2, 3],
                    [3, 4, 5, 6, 7, 1, 2],
                    [2, 3, 4, 5, 6, 7, 1],
                    [1, 2, 3, 4, 5, 6, 7]
                ];
            b.DATE = function(d, f, k) {
                d = e.parseNumber(d);
                f = e.parseNumber(f);
                k = e.parseNumber(k);
                return e.anyIsError(d, f, k) ? h.value : 0 > d || 0 > f || 0 > k ? h.num : new Date(d, f - 1, k)
            };
            b.DATEVALUE = function(d) {
                if ("string" !== typeof d) return h.value;
                d = Date.parse(d);
                return isNaN(d) ? h.value : -22038912E5 >= d ? (d - a) / 864E5 + 1 : (d - a) / 864E5 + 2
            };
            b.DAY = function(d) {
                d = e.parseDate(d);
                return d instanceof Error ? d : d.getDate()
            };
            b.DAYS = function(d, f) {
                d = e.parseDate(d);
                f = e.parseDate(f);
                return d instanceof Error ? d : f instanceof Error ? f : g(d) - g(f)
            };
            b.DAYS360 = function(d, f, k) {};
            b.EDATE = function(d, f) {
                d = e.parseDate(d);
                if (d instanceof Error) return d;
                if (isNaN(f)) return h.value;
                f = parseInt(f, 10);
                d.setMonth(d.getMonth() + f);
                return g(d)
            };
            b.EOMONTH = function(d, f) {
                d = e.parseDate(d);
                if (d instanceof Error) return d;
                if (isNaN(f)) return h.value;
                f = parseInt(f, 10);
                return g(new Date(d.getFullYear(), d.getMonth() + f + 1, 0))
            };
            b.HOUR = function(d) {
                d = e.parseDate(d);
                return d instanceof Error ? d : d.getHours()
            };
            b.INTERVAL = function(d) {
                if ("number" !== typeof d && "string" !== typeof d) return h.value;
                d = parseInt(d, 10);
                var f = Math.floor(d / 94608E4);
                d %= 94608E4;
                var k = Math.floor(d / 2592E3);
                d %= 2592E3;
                var l = Math.floor(d / 86400);
                d %= 86400;
                var m = Math.floor(d / 3600);
                d %= 3600;
                var n = Math.floor(d / 60);
                d %= 60;
                return "P" + (0 < f ? f + "Y" : "") + (0 < k ? k + "M" : "") + (0 < l ? l + "D" : "") + "T" + (0 < m ? m + "H" : "") + (0 < n ? n + "M" : "") + (0 < d ? d + "S" : "")
            };
            b.ISOWEEKNUM = function(d) {
                d = e.parseDate(d);
                if (d instanceof Error) return d;
                d.setHours(0, 0, 0);
                d.setDate(d.getDate() + 4 - (d.getDay() ||
                    7));
                var f = new Date(d.getFullYear(), 0, 1);
                return Math.ceil(((d - f) / 864E5 + 1) / 7)
            };
            b.MINUTE = function(d) {
                d = e.parseDate(d);
                return d instanceof Error ? d : d.getMinutes()
            };
            b.MONTH = function(d) {
                d = e.parseDate(d);
                return d instanceof Error ? d : d.getMonth() + 1
            };
            b.NETWORKDAYS = function(d, f, k) {};
            b.NETWORKDAYS.INTL = function(d, f, k, l) {};
            b.NOW = function() {
                return new Date
            };
            b.SECOND = function(d) {
                d = e.parseDate(d);
                return d instanceof Error ? d : d.getSeconds()
            };
            b.TIME = function(d, f, k) {
                d = e.parseNumber(d);
                f = e.parseNumber(f);
                k = e.parseNumber(k);
                return e.anyIsError(d, f, k) ? h.value : 0 > d || 0 > f || 0 > k ? h.num : (3600 * d + 60 * f + k) / 86400
            };
            b.TIMEVALUE = function(d) {
                d = e.parseDate(d);
                return d instanceof Error ? d : (3600 * d.getHours() + 60 * d.getMinutes() + d.getSeconds()) / 86400
            };
            b.TODAY = function() {
                return new Date
            };
            b.WEEKDAY = function(d, f) {
                d = e.parseDate(d);
                if (d instanceof Error) return d;
                void 0 === f && (f = 1);
                d = d.getDay();
                return c[f][d]
            };
            b.WEEKNUM = function(d, f) {};
            b.WORKDAY = function(d, f, k) {};
            b.WORKDAY.INTL = function(d, f, k, l) {};
            b.YEAR = function(d) {
                d = e.parseDate(d);
                return d instanceof
                Error ? d : d.getFullYear()
            };
            b.YEARFRAC = function(d, f, k) {};
            return b
        }();
        t.database = function() {
            function g(a, c) {
                for (var d = {}, f = 1; f < a[0].length; ++f) d[f] = !0;
                var k = c[0].length;
                for (f = 1; f < c.length; ++f) c[f].length > k && (k = c[f].length);
                for (f = 1; f < a.length; ++f)
                    for (var l = 1; l < a[f].length; ++l) {
                        for (var m = !1, n = !1, r = 0; r < c.length; ++r) {
                            var u = c[r];
                            if (!(u.length < k) && a[f][0] === u[0]) {
                                n = !0;
                                for (var w = 1; w < u.length; ++w) m = m || eval(a[f][l] + u[w])
                            }
                        }
                        n && (d[l] = d[l] && m)
                    }
                c = [];
                for (k = 0; k < a[0].length; ++k) d[k] && c.push(k - 1);
                return c
            }
            var b = {
                FINDFIELD: function(a,
                    c) {
                    for (var d = null, f = 0; f < a.length; f++)
                        if (a[f][0] === c) {
                            d = f;
                            break
                        } return null == d ? h.value : d
                },
                DAVERAGE: function(a, c, d) {
                    if (isNaN(c) && "string" !== typeof c) return h.value;
                    d = g(a, d);
                    "string" === typeof c ? (c = b.FINDFIELD(a, c), a = e.rest(a[c])) : a = e.rest(a[c]);
                    for (var f = c = 0; f < d.length; f++) c += a[d[f]];
                    return 0 === d.length ? h.div0 : c / d.length
                },
                DCOUNT: function(a, c, d) {},
                DCOUNTA: function(a, c, d) {},
                DGET: function(a, c, d) {
                    if (isNaN(c) && "string" !== typeof c) return h.value;
                    d = g(a, d);
                    "string" === typeof c ? (c = b.FINDFIELD(a, c), a = e.rest(a[c])) :
                        a = e.rest(a[c]);
                    return 0 === d.length ? h.value : 1 < d.length ? h.num : a[d[0]]
                },
                DMAX: function(a, c, d) {
                    if (isNaN(c) && "string" !== typeof c) return h.value;
                    d = g(a, d);
                    "string" === typeof c ? (c = b.FINDFIELD(a, c), a = e.rest(a[c])) : a = e.rest(a[c]);
                    c = a[d[0]];
                    for (var f = 1; f < d.length; f++) c < a[d[f]] && (c = a[d[f]]);
                    return c
                },
                DMIN: function(a, c, d) {
                    if (isNaN(c) && "string" !== typeof c) return h.value;
                    d = g(a, d);
                    "string" === typeof c ? (c = b.FINDFIELD(a, c), a = e.rest(a[c])) : a = e.rest(a[c]);
                    c = a[d[0]];
                    for (var f = 1; f < d.length; f++) c > a[d[f]] && (c = a[d[f]]);
                    return c
                },
                DPRODUCT: function(a, c, d) {
                    if (isNaN(c) && "string" !== typeof c) return h.value;
                    d = g(a, d);
                    if ("string" === typeof c) {
                        c = b.FINDFIELD(a, c);
                        var f = e.rest(a[c])
                    } else f = e.rest(a[c]);
                    a = [];
                    for (c = 0; c < d.length; c++) a[c] = f[d[c]];
                    if (d = a)
                        for (a = [], c = 0; c < d.length; ++c) d[c] && a.push(d[c]);
                    else a = d;
                    d = 1;
                    for (c = 0; c < a.length; c++) d *= a[c];
                    return d
                },
                DSTDEV: function(a, c, d) {},
                DSTDEVP: function(a, c, d) {},
                DSUM: function(a, c, d) {},
                DVAR: function(a, c, d) {},
                DVARP: function(a, c, d) {},
                MATCH: function(a, c, d) {
                    if (!a && !c) return h.na;
                    2 === arguments.length && (d =
                        1);
                    if (!(c instanceof Array) || -1 !== d && 0 !== d && 1 !== d) return h.na;
                    for (var f, k, l = 0; l < c.length; l++)
                        if (1 === d) {
                            if (c[l] === a) return l + 1;
                            c[l] < a && (k ? c[l] > k && (f = l + 1, k = c[l]) : (f = l + 1, k = c[l]))
                        } else if (0 === d)
                        if ("string" === typeof a) {
                            if (a = a.replace(/\?/g, "."), c[l].toLowerCase().match(a.toLowerCase())) return l + 1
                        } else {
                            if (c[l] === a) return l + 1
                        }
                    else if (-1 === d) {
                        if (c[l] === a) return l + 1;
                        c[l] > a && (k ? c[l] < k && (f = l + 1, k = c[l]) : (f = l + 1, k = c[l]))
                    }
                    return f ? f : h.na
                }
            };
            return b
        }();
        t.engineering = function() {
            var g = {
                BESSELI: function(b, a) {},
                BESSELJ: function(b,
                    a) {},
                BESSELK: function(b, a) {},
                BESSELY: function(b, a) {},
                BIN2DEC: function(b) {
                    if (!/^[01]{1,10}$/.test(b)) return h.num;
                    var a = parseInt(b, 2);
                    b = b.toString();
                    return 10 === b.length && "1" === b.substring(0, 1) ? parseInt(b.substring(1), 2) - 512 : a
                },
                BIN2HEX: function(b, a) {
                    if (!/^[01]{1,10}$/.test(b)) return h.num;
                    var c = b.toString();
                    if (10 === c.length && "1" === c.substring(0, 1)) return (0xfffffffe00 + parseInt(c.substring(1), 2)).toString(16);
                    b = parseInt(b, 2).toString(16);
                    if (void 0 === a) return b;
                    if (isNaN(a)) return h.value;
                    if (0 > a) return h.num;
                    a = Math.floor(a);
                    return a >= b.length ? REPT("0", a - b.length) + b : h.num
                },
                BIN2OCT: function(b, a) {
                    if (!/^[01]{1,10}$/.test(b)) return h.num;
                    var c = b.toString();
                    if (10 === c.length && "1" === c.substring(0, 1)) return (1073741312 + parseInt(c.substring(1), 2)).toString(8);
                    b = parseInt(b, 2).toString(8);
                    if (void 0 === a) return b;
                    if (isNaN(a)) return h.value;
                    if (0 > a) return h.num;
                    a = Math.floor(a);
                    return a >= b.length ? REPT("0", a - b.length) + b : h.num
                },
                BITAND: function(b, a) {
                    b = e.parseNumber(b);
                    a = e.parseNumber(a);
                    return e.anyIsError(b, a) ? h.value : 0 >
                        b || 0 > a || Math.floor(b) !== b || Math.floor(a) !== a || 0xffffffffffff < b || 0xffffffffffff < a ? h.num : b & a
                },
                BITLSHIFT: function(b, a) {
                    b = e.parseNumber(b);
                    a = e.parseNumber(a);
                    return e.anyIsError(b, a) ? h.value : 0 > b || Math.floor(b) !== b || 0xffffffffffff < b || 53 < Math.abs(a) ? h.num : 0 <= a ? b << a : b >> -a
                },
                BITOR: function(b, a) {
                    b = e.parseNumber(b);
                    a = e.parseNumber(a);
                    return e.anyIsError(b, a) ? h.value : 0 > b || 0 > a || Math.floor(b) !== b || Math.floor(a) !== a || 0xffffffffffff < b || 0xffffffffffff < a ? h.num : b | a
                },
                BITRSHIFT: function(b, a) {
                    b = e.parseNumber(b);
                    a = e.parseNumber(a);
                    return e.anyIsError(b, a) ? h.value : 0 > b || Math.floor(b) !== b || 0xffffffffffff < b || 53 < Math.abs(a) ? h.num : 0 <= a ? b >> a : b << -a
                },
                BITXOR: function(b, a) {
                    b = e.parseNumber(b);
                    a = e.parseNumber(a);
                    return e.anyIsError(b, a) ? h.value : 0 > b || 0 > a || Math.floor(b) !== b || Math.floor(a) !== a || 0xffffffffffff < b || 0xffffffffffff < a ? h.num : b ^ a
                },
                COMPLEX: function(b, a, c) {
                    b = e.parseNumber(b);
                    a = e.parseNumber(a);
                    if (e.anyIsError(b, a)) return b;
                    c = void 0 === c ? "i" : c;
                    return "i" !== c && "j" !== c ? h.value : 0 === b && 0 === a ? 0 : 0 === b ? 1 === a ? c : a.toString() + c : 0 === a ? b.toString() :
                        b.toString() + (0 < a ? "+" : "") + (1 === a ? c : a.toString() + c)
                },
                CONVERT: function(b, a, c) {
                    b = e.parseNumber(b);
                    if (b instanceof Error) return b;
                    for (var d = [
                            ["a.u. of action", "?", null, "action", !1, !1, 1.05457168181818E-34],
                            ["a.u. of charge", "e", null, "electric_charge", !1, !1, 1.60217653141414E-19],
                            ["a.u. of energy", "Eh", null, "energy", !1, !1, 4.35974417757576E-18],
                            ["a.u. of length", "a?", null, "length", !1, !1, 5.29177210818182E-11],
                            ["a.u. of mass", "m?", null, "mass", !1, !1, 9.10938261616162E-31],
                            ["a.u. of time", "?/Eh", null, "time", !1,
                                !1, 2.41888432650516E-17
                            ],
                            ["admiralty knot", "admkn", null, "speed", !1, !0, .514773333],
                            ["ampere", "A", null, "electric_current", !0, !1, 1],
                            ["ampere per meter", "A/m", null, "magnetic_field_intensity", !0, !1, 1],
                            ["\u00e5ngstr\u00f6m", "\u00c5", ["ang"], "length", !1, !0, 1E-10],
                            ["are", "ar", null, "area", !1, !0, 100],
                            ["astronomical unit", "ua", null, "length", !1, !1, 1.49597870691667E-11],
                            ["bar", "bar", null, "pressure", !1, !1, 1E5],
                            ["barn", "b", null, "area", !1, !1, 1E-28],
                            ["becquerel", "Bq", null, "radioactivity", !0, !1, 1],
                            ["bit", "bit", ["b"],
                                "information", !1, !0, 1
                            ],
                            ["btu", "BTU", ["btu"], "energy", !1, !0, 1055.05585262],
                            ["byte", "byte", null, "information", !1, !0, 8],
                            ["candela", "cd", null, "luminous_intensity", !0, !1, 1],
                            ["candela per square metre", "cd/m?", null, "luminance", !0, !1, 1],
                            ["coulomb", "C", null, "electric_charge", !0, !1, 1],
                            ["cubic \u00e5ngstr\u00f6m", "ang3", ["ang^3"], "volume", !1, !0, 1E-30],
                            ["cubic foot", "ft3", ["ft^3"], "volume", !1, !0, .028316846592],
                            ["cubic inch", "in3", ["in^3"], "volume", !1, !0, 1.6387064E-5],
                            ["cubic light-year", "ly3", ["ly^3"], "volume",
                                !1, !0, 8.46786664623715E-47
                            ],
                            ["cubic metre", "m?", null, "volume", !0, !0, 1],
                            ["cubic mile", "mi3", ["mi^3"], "volume", !1, !0, 4.16818182544058E9],
                            ["cubic nautical mile", "Nmi3", ["Nmi^3"], "volume", !1, !0, 6352182208],
                            ["cubic Pica", "Pica3", ["Picapt3", "Pica^3", "Picapt^3"], "volume", !1, !0, 7.58660370370369E-8],
                            ["cubic yard", "yd3", ["yd^3"], "volume", !1, !0, .764554857984],
                            ["cup", "cup", null, "volume", !1, !0, 2.365882365E-4],
                            ["dalton", "Da", ["u"], "mass", !1, !1, 1.66053886282828E-27],
                            ["day", "d", ["day"], "time", !1, !0, 86400],
                            ["degree",
                                "\u00b0", null, "angle", !1, !1, .0174532925199433
                            ],
                            ["degrees Rankine", "Rank", null, "temperature", !1, !0, .555555555555556],
                            ["dyne", "dyn", ["dy"], "force", !1, !0, 1E-5],
                            ["electronvolt", "eV", ["ev"], "energy", !1, !0, 1.60217656514141],
                            ["ell", "ell", null, "length", !1, !0, 1.143],
                            ["erg", "erg", ["e"], "energy", !1, !0, 1E-7],
                            ["farad", "F", null, "electric_capacitance", !0, !1, 1],
                            ["fluid ounce", "oz", null, "volume", !1, !0, 2.95735295625E-5],
                            ["foot", "ft", null, "length", !1, !0, .3048],
                            ["foot-pound", "flb", null, "energy", !1, !0, 1.3558179483314],
                            ["gal", "Gal", null, "acceleration", !1, !1, .01],
                            ["gallon", "gal", null, "volume", !1, !0, .003785411784],
                            ["gauss", "G", ["ga"], "magnetic_flux_density", !1, !0, 1],
                            ["grain", "grain", null, "mass", !1, !0, 6.47989E-5],
                            ["gram", "g", null, "mass", !1, !0, .001],
                            ["gray", "Gy", null, "absorbed_dose", !0, !1, 1],
                            ["gross registered ton", "GRT", ["regton"], "volume", !1, !0, 2.8316846592],
                            ["hectare", "ha", null, "area", !1, !0, 1E4],
                            ["henry", "H", null, "inductance", !0, !1, 1],
                            ["hertz", "Hz", null, "frequency", !0, !1, 1],
                            ["horsepower", "HP", ["h"], "power", !1, !0,
                                745.69987158227
                            ],
                            ["horsepower-hour", "HPh", ["hh", "hph"], "energy", !1, !0, 2684519.538],
                            ["hour", "h", ["hr"], "time", !1, !0, 3600],
                            ["imperial gallon (U.K.)", "uk_gal", null, "volume", !1, !0, .00454609],
                            ["imperial hundredweight", "lcwt", ["uk_cwt", "hweight"], "mass", !1, !0, 50.802345],
                            ["imperial quart (U.K)", "uk_qt", null, "volume", !1, !0, .0011365225],
                            ["imperial ton", "brton", ["uk_ton", "LTON"], "mass", !1, !0, 1016.046909],
                            ["inch", "in", null, "length", !1, !0, .0254],
                            ["international acre", "uk_acre", null, "area", !1, !0, 4046.8564224],
                            ["IT calorie", "cal", null, "energy", !1, !0, 4.1868],
                            ["joule", "J", null, "energy", !0, !0, 1],
                            ["katal", "kat", null, "catalytic_activity", !0, !1, 1],
                            ["kelvin", "K", ["kel"], "temperature", !0, !0, 1],
                            ["kilogram", "kg", null, "mass", !0, !0, 1],
                            ["knot", "kn", null, "speed", !1, !0, .514444444444444],
                            ["light-year", "ly", null, "length", !1, !0, 9460730472580800],
                            ["litre", "L", ["l", "lt"], "volume", !1, !0, .001],
                            ["lumen", "lm", null, "luminous_flux", !0, !1, 1],
                            ["lux", "lx", null, "illuminance", !0, !1, 1],
                            ["maxwell", "Mx", null, "magnetic_flux", !1, !1, 1E-18],
                            ["measurement ton", "MTON", null, "volume", !1, !0, 1.13267386368],
                            ["meter per hour", "m/h", ["m/hr"], "speed", !1, !0, 2.7777777777778E-4],
                            ["meter per second", "m/s", ["m/sec"], "speed", !0, !0, 1],
                            ["meter per second squared", "m?s??", null, "acceleration", !0, !1, 1],
                            ["parsec", "pc", ["parsec"], "length", !1, !0, 0x6da012f958ee1c],
                            ["meter squared per second", "m?/s", null, "kinematic_viscosity", !0, !1, 1],
                            ["metre", "m", null, "length", !0, !0, 1],
                            ["miles per hour", "mph", null, "speed", !1, !0, .44704],
                            ["millimetre of mercury", "mmHg", null, "pressure",
                                !1, !1, 133.322
                            ],
                            ["minute", "?", null, "angle", !1, !1, 2.90888208665722E-4],
                            ["minute", "min", ["mn"], "time", !1, !0, 60],
                            ["modern teaspoon", "tspm", null, "volume", !1, !0, 5E-6],
                            ["mole", "mol", null, "amount_of_substance", !0, !1, 1],
                            ["morgen", "Morgen", null, "area", !1, !0, 2500],
                            ["n.u. of action", "?", null, "action", !1, !1, 1.05457168181818E-34],
                            ["n.u. of mass", "m?", null, "mass", !1, !1, 9.10938261616162E-31],
                            ["n.u. of speed", "c?", null, "speed", !1, !1, 299792458],
                            ["n.u. of time", "?/(me?c??)", null, "time", !1, !1, 1.28808866778687E-21],
                            ["nautical mile",
                                "M", ["Nmi"], "length", !1, !0, 1852
                            ],
                            ["newton", "N", null, "force", !0, !0, 1],
                            ["\u0153rsted", "Oe ", null, "magnetic_field_intensity", !1, !1, 79.5774715459477],
                            ["ohm", "\u03a9", null, "electric_resistance", !0, !1, 1],
                            ["ounce mass", "ozm", null, "mass", !1, !0, .028349523125],
                            ["pascal", "Pa", null, "pressure", !0, !1, 1],
                            ["pascal second", "Pa?s", null, "dynamic_viscosity", !0, !1, 1],
                            ["pferdest\u00e4rke", "PS", null, "power", !1, !0, 735.49875],
                            ["phot", "ph", null, "illuminance", !1, !1, 1E-4],
                            ["pica (1/6 inch)", "pica", null, "length", !1, !0, 3.5277777777778E-4],
                            ["pica (1/72 inch)", "Pica", ["Picapt"], "length", !1, !0, .00423333333333333],
                            ["poise", "P", null, "dynamic_viscosity", !1, !1, .1],
                            ["pond", "pond", null, "force", !1, !0, .00980665],
                            ["pound force", "lbf", null, "force", !1, !0, 4.4482216152605],
                            ["pound mass", "lbm", null, "mass", !1, !0, .45359237],
                            ["quart", "qt", null, "volume", !1, !0, 9.46352946E-4],
                            ["radian", "rad", null, "angle", !0, !1, 1],
                            ["second", "?", null, "angle", !1, !1, 4.84813681109536E-6],
                            ["second", "s", ["sec"], "time", !0, !0, 1],
                            ["short hundredweight", "cwt", ["shweight"], "mass", !1,
                                !0, 45.359237
                            ],
                            ["siemens", "S", null, "electrical_conductance", !0, !1, 1],
                            ["sievert", "Sv", null, "equivalent_dose", !0, !1, 1],
                            ["slug", "sg", null, "mass", !1, !0, 14.59390294],
                            ["square \u00e5ngstr\u00f6m", "ang2", ["ang^2"], "area", !1, !0, 1E-20],
                            ["square foot", "ft2", ["ft^2"], "area", !1, !0, .09290304],
                            ["square inch", "in2", ["in^2"], "area", !1, !0, 6.4516E-4],
                            ["square light-year", "ly2", ["ly^2"], "area", !1, !0, 8.95054210748189E31],
                            ["square meter", "m?", null, "area", !0, !0, 1],
                            ["square mile", "mi2", ["mi^2"], "area", !1, !0, 2589988.110336],
                            ["square nautical mile", "Nmi2", ["Nmi^2"], "area", !1, !0, 3429904],
                            ["square Pica", "Pica2", ["Picapt2", "Pica^2", "Picapt^2"], "area", !1, !0, 1.792111111111E-5],
                            ["square yard", "yd2", ["yd^2"], "area", !1, !0, .83612736],
                            ["statute mile", "mi", null, "length", !1, !0, 1609.344],
                            ["steradian", "sr", null, "solid_angle", !0, !1, 1],
                            ["stilb", "sb", null, "luminance", !1, !1, 1E-4],
                            ["stokes", "St", null, "kinematic_viscosity", !1, !1, 1E-4],
                            ["stone", "stone", null, "mass", !1, !0, 6.35029318],
                            ["tablespoon", "tbs", null, "volume", !1, !0, 1.47868E-5],
                            ["teaspoon",
                                "tsp", null, "volume", !1, !0, 4.92892E-6
                            ],
                            ["tesla", "T", null, "magnetic_flux_density", !0, !0, 1],
                            ["thermodynamic calorie", "c", null, "energy", !1, !0, 4.184],
                            ["ton", "ton", null, "mass", !1, !0, 907.18474],
                            ["tonne", "t", null, "mass", !1, !1, 1E3],
                            ["U.K. pint", "uk_pt", null, "volume", !1, !0, 5.6826125E-4],
                            ["U.S. bushel", "bushel", null, "volume", !1, !0, .03523907],
                            ["U.S. oil barrel", "barrel", null, "volume", !1, !0, .158987295],
                            ["U.S. pint", "pt", ["us_pt"], "volume", !1, !0, 4.73176473E-4],
                            ["U.S. survey mile", "survey_mi", null, "length", !1, !0,
                                1609.347219
                            ],
                            ["U.S. survey/statute acre", "us_acre", null, "area", !1, !0, 4046.87261],
                            ["volt", "V", null, "voltage", !0, !1, 1],
                            ["watt", "W", null, "power", !0, !0, 1],
                            ["watt-hour", "Wh", ["wh"], "energy", !1, !0, 3600],
                            ["weber", "Wb", null, "magnetic_flux", !0, !1, 1],
                            ["yard", "yd", null, "length", !1, !0, .9144],
                            ["year", "yr", null, "time", !1, !0, 31557600]
                        ], f = {
                            Yi: ["yobi", 80, 1.2089258196146292E24, "Yi", "yotta"],
                            Zi: ["zebi", 70, 1.1805916207174113E21, "Zi", "zetta"],
                            Ei: ["exbi", 60, 0x1000000000000000, "Ei", "exa"],
                            Pi: ["pebi", 50, 0x4000000000000,
                                "Pi", "peta"
                            ],
                            Ti: ["tebi", 40, 1099511627776, "Ti", "tera"],
                            Gi: ["gibi", 30, 1073741824, "Gi", "giga"],
                            Mi: ["mebi", 20, 1048576, "Mi", "mega"],
                            ki: ["kibi", 10, 1024, "ki", "kilo"]
                        }, k = {
                            Y: ["yotta", 1E24, "Y"],
                            Z: ["zetta", 1E21, "Z"],
                            E: ["exa", 1E18, "E"],
                            P: ["peta", 1E15, "P"],
                            T: ["tera", 1E12, "T"],
                            G: ["giga", 1E9, "G"],
                            M: ["mega", 1E6, "M"],
                            k: ["kilo", 1E3, "k"],
                            h: ["hecto", 100, "h"],
                            e: ["dekao", 10, "e"],
                            d: ["deci", .1, "d"],
                            c: ["centi", .01, "c"],
                            m: ["milli", .001, "m"],
                            u: ["micro", 1E-6, "u"],
                            n: ["nano", 1E-9, "n"],
                            p: ["pico", 1E-12, "p"],
                            f: ["femto", 1E-15, "f"],
                            a: ["atto", 1E-18, "a"],
                            z: ["zepto", 1E-21, "z"],
                            y: ["yocto", 1E-24, "y"]
                        }, l = null, m = null, n = a, r = c, u = 1, w = 1, x, v = 0; v < d.length; v++) {
                        x = null === d[v][2] ? [] : d[v][2];
                        if (d[v][1] === n || 0 <= x.indexOf(n)) l = d[v];
                        if (d[v][1] === r || 0 <= x.indexOf(r)) m = d[v]
                    }
                    if (null === l)
                        for (x = f[a.substring(0, 2)], v = k[a.substring(0, 1)], "da" === a.substring(0, 2) && (v = ["dekao", 10, "da"]), x ? (u = x[2], n = a.substring(2)) : v && (u = v[1], n = a.substring(v[2].length)), a = 0; a < d.length; a++)
                            if (x = null === d[a][2] ? [] : d[a][2], d[a][1] === n || 0 <= x.indexOf(n)) l = d[a];
                    if (null === m)
                        for (f =
                            f[c.substring(0, 2)], k = k[c.substring(0, 1)], "da" === c.substring(0, 2) && (k = ["dekao", 10, "da"]), f ? (w = f[2], r = c.substring(2)) : k && (w = k[1], r = c.substring(k[2].length)), c = 0; c < d.length; c++)
                            if (x = null === d[c][2] ? [] : d[c][2], d[c][1] === r || 0 <= x.indexOf(r)) m = d[c];
                    return null === l || null === m || l[3] !== m[3] ? h.na : b * l[6] * u / (m[6] * w)
                },
                DEC2BIN: function(b, a) {
                    b = e.parseNumber(b);
                    if (b instanceof Error) return b;
                    if (!/^-?[0-9]{1,3}$/.test(b) || -512 > b || 511 < b) return h.num;
                    if (0 > b) return "1" + REPT("0", 9 - (512 + b).toString(2).length) + (512 + b).toString(2);
                    b = parseInt(b, 10).toString(2);
                    if ("undefined" === typeof a) return b;
                    if (isNaN(a)) return h.value;
                    if (0 > a) return h.num;
                    a = Math.floor(a);
                    return a >= b.length ? REPT("0", a - b.length) + b : h.num
                },
                DEC2HEX: function(b, a) {
                    b = e.parseNumber(b);
                    if (b instanceof Error) return b;
                    if (!/^-?[0-9]{1,12}$/.test(b) || -549755813888 > b || 549755813887 < b) return h.num;
                    if (0 > b) return (1099511627776 + b).toString(16);
                    b = parseInt(b, 10).toString(16);
                    if ("undefined" === typeof a) return b;
                    if (isNaN(a)) return h.value;
                    if (0 > a) return h.num;
                    a = Math.floor(a);
                    return a >=
                        b.length ? REPT("0", a - b.length) + b : h.num
                },
                DEC2OCT: function(b, a) {
                    b = e.parseNumber(b);
                    if (b instanceof Error) return b;
                    if (!/^-?[0-9]{1,9}$/.test(b) || -536870912 > b || 536870911 < b) return h.num;
                    if (0 > b) return (1073741824 + b).toString(8);
                    b = parseInt(b, 10).toString(8);
                    if ("undefined" === typeof a) return b;
                    if (isNaN(a)) return h.value;
                    if (0 > a) return h.num;
                    a = Math.floor(a);
                    return a >= b.length ? REPT("0", a - b.length) + b : h.num
                },
                DELTA: function(b, a) {
                    a = void 0 === a ? 0 : a;
                    b = e.parseNumber(b);
                    a = e.parseNumber(a);
                    return e.anyIsError(b, a) ? h.value :
                        b === a ? 1 : 0
                },
                ERF: function(b, a) {}
            };
            g.ERF.PRECISE = function() {};
            g.ERFC = function(b) {};
            g.ERFC.PRECISE = function() {};
            g.GESTEP = function(b, a) {
                a = a || 0;
                b = e.parseNumber(b);
                return e.anyIsError(a, b) ? b : b >= a ? 1 : 0
            };
            g.HEX2BIN = function(b, a) {
                if (!/^[0-9A-Fa-f]{1,10}$/.test(b)) return h.num;
                var c = 10 === b.length && "f" === b.substring(0, 1).toLowerCase() ? !0 : !1;
                b = c ? parseInt(b, 16) - 1099511627776 : parseInt(b, 16);
                if (-512 > b || 511 < b) return h.num;
                if (c) return "1" + REPT("0", 9 - (512 + b).toString(2).length) + (512 + b).toString(2);
                c = b.toString(2);
                if (void 0 ===
                    a) return c;
                if (isNaN(a)) return h.value;
                if (0 > a) return h.num;
                a = Math.floor(a);
                return a >= c.length ? REPT("0", a - c.length) + c : h.num
            };
            g.HEX2DEC = function(b) {
                if (!/^[0-9A-Fa-f]{1,10}$/.test(b)) return h.num;
                b = parseInt(b, 16);
                return 549755813888 <= b ? b - 1099511627776 : b
            };
            g.HEX2OCT = function(b, a) {
                if (!/^[0-9A-Fa-f]{1,10}$/.test(b)) return h.num;
                b = parseInt(b, 16);
                if (536870911 < b && 0xffe0000000 > b) return h.num;
                if (0xffe0000000 <= b) return (b - 0xffc0000000).toString(8);
                b = b.toString(8);
                if (void 0 === a) return b;
                if (isNaN(a)) return h.value;
                if (0 > a) return h.num;
                a = Math.floor(a);
                return a >= b.length ? REPT("0", a - b.length) + b : h.num
            };
            g.IMABS = function(b) {
                var a = g.IMREAL(b);
                b = g.IMAGINARY(b);
                return e.anyIsError(a, b) ? h.value : Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2))
            };
            g.IMAGINARY = function(b) {
                if (void 0 === b || !0 === b || !1 === b) return h.value;
                if (0 === b || "0" === b) return 0;
                if (0 <= ["i", "j"].indexOf(b)) return 1;
                b = b.replace("+i", "+1i").replace("-i", "-1i").replace("+j", "+1j").replace("-j", "-1j");
                var a = b.indexOf("+"),
                    c = b.indexOf("-");
                0 === a && (a = b.indexOf("+", 1));
                0 === c &&
                    (c = b.indexOf("-", 1));
                var d = b.substring(b.length - 1, b.length);
                d = "i" === d || "j" === d;
                return 0 <= a || 0 <= c ? d ? 0 <= a ? isNaN(b.substring(0, a)) || isNaN(b.substring(a + 1, b.length - 1)) ? h.num : Number(b.substring(a + 1, b.length - 1)) : isNaN(b.substring(0, c)) || isNaN(b.substring(c + 1, b.length - 1)) ? h.num : -Number(b.substring(c + 1, b.length - 1)) : h.num : d ? isNaN(b.substring(0, b.length - 1)) ? h.num : b.substring(0, b.length - 1) : isNaN(b) ? h.num : 0
            };
            g.IMARGUMENT = function(b) {
                var a = g.IMREAL(b);
                b = g.IMAGINARY(b);
                return e.anyIsError(a, b) ? h.value : 0 === a && 0 ===
                    b ? h.div0 : 0 === a && 0 < b ? Math.PI / 2 : 0 === a && 0 > b ? -Math.PI / 2 : 0 === b && 0 < a ? 0 : 0 === b && 0 > a ? -Math.PI : 0 < a ? Math.atan(b / a) : 0 > a && 0 <= b ? Math.atan(b / a) + Math.PI : Math.atan(b / a) - Math.PI
            };
            g.IMCONJUGATE = function(b) {
                var a = g.IMREAL(b),
                    c = g.IMAGINARY(b);
                if (e.anyIsError(a, c)) return h.value;
                var d = b.substring(b.length - 1);
                return 0 !== c ? g.COMPLEX(a, -c, "i" === d || "j" === d ? d : "i") : b
            };
            g.IMCOS = function(b) {
                var a = g.IMREAL(b),
                    c = g.IMAGINARY(b);
                if (e.anyIsError(a, c)) return h.value;
                b = b.substring(b.length - 1);
                return g.COMPLEX(Math.cos(a) * (Math.exp(c) +
                    Math.exp(-c)) / 2, -Math.sin(a) * (Math.exp(c) - Math.exp(-c)) / 2, "i" === b || "j" === b ? b : "i")
            };
            g.IMCOSH = function(b) {
                var a = g.IMREAL(b),
                    c = g.IMAGINARY(b);
                if (e.anyIsError(a, c)) return h.value;
                b = b.substring(b.length - 1);
                return g.COMPLEX(Math.cos(c) * (Math.exp(a) + Math.exp(-a)) / 2, Math.sin(c) * (Math.exp(a) - Math.exp(-a)) / 2, "i" === b || "j" === b ? b : "i")
            };
            g.IMCOT = function(b) {
                var a = g.IMREAL(b),
                    c = g.IMAGINARY(b);
                return e.anyIsError(a, c) ? h.value : g.IMDIV(g.IMCOS(b), g.IMSIN(b))
            };
            g.IMDIV = function(b, a) {
                var c = g.IMREAL(b),
                    d = g.IMAGINARY(b),
                    f = g.IMREAL(a),
                    k = g.IMAGINARY(a);
                if (e.anyIsError(c, d, f, k)) return h.value;
                b = b.substring(b.length - 1);
                var l = a.substring(a.length - 1);
                a = "i";
                "j" === b ? a = "j" : "j" === l && (a = "j");
                if (0 === f && 0 === k) return h.num;
                b = f * f + k * k;
                return g.COMPLEX((c * f + d * k) / b, (d * f - c * k) / b, a)
            };
            g.IMEXP = function(b) {
                var a = g.IMREAL(b),
                    c = g.IMAGINARY(b);
                if (e.anyIsError(a, c)) return h.value;
                b = b.substring(b.length - 1);
                a = Math.exp(a);
                return g.COMPLEX(a * Math.cos(c), a * Math.sin(c), "i" === b || "j" === b ? b : "i")
            };
            g.IMLN = function(b) {
                var a = g.IMREAL(b),
                    c = g.IMAGINARY(b);
                if (e.anyIsError(a, c)) return h.value;
                b = b.substring(b.length - 1);
                return g.COMPLEX(Math.log(Math.sqrt(a * a + c * c)), Math.atan(c / a), "i" === b || "j" === b ? b : "i")
            };
            g.IMLOG10 = function(b) {
                var a = g.IMREAL(b),
                    c = g.IMAGINARY(b);
                if (e.anyIsError(a, c)) return h.value;
                b = b.substring(b.length - 1);
                return g.COMPLEX(Math.log(Math.sqrt(a * a + c * c)) / Math.log(10), Math.atan(c / a) / Math.log(10), "i" === b || "j" === b ? b : "i")
            };
            g.IMLOG2 = function(b) {
                var a = g.IMREAL(b),
                    c = g.IMAGINARY(b);
                if (e.anyIsError(a, c)) return h.value;
                b = b.substring(b.length - 1);
                return g.COMPLEX(Math.log(Math.sqrt(a *
                    a + c * c)) / Math.log(2), Math.atan(c / a) / Math.log(2), "i" === b || "j" === b ? b : "i")
            };
            g.IMPOWER = function(b, a) {
                a = e.parseNumber(a);
                var c = g.IMREAL(b),
                    d = g.IMAGINARY(b);
                if (e.anyIsError(a, c, d)) return h.value;
                c = b.substring(b.length - 1);
                c = "i" === c || "j" === c ? c : "i";
                d = Math.pow(g.IMABS(b), a);
                b = g.IMARGUMENT(b);
                return g.COMPLEX(d * Math.cos(a * b), d * Math.sin(a * b), c)
            };
            g.IMPRODUCT = function() {
                for (var b = arguments[0], a = 1; a < arguments.length; a++) {
                    var c = g.IMREAL(b);
                    b = g.IMAGINARY(b);
                    var d = g.IMREAL(arguments[a]),
                        f = g.IMAGINARY(arguments[a]);
                    if (e.anyIsError(c, b, d, f)) return h.value;
                    b = g.COMPLEX(c * d - b * f, c * f + b * d)
                }
                return b
            };
            g.IMREAL = function(b) {
                if (void 0 === b || !0 === b || !1 === b) return h.value;
                if (0 === b || "0" === b || 0 <= "i +i 1i +1i -i -1i j +j 1j +1j -j -1j".split(" ").indexOf(b)) return 0;
                var a = b.indexOf("+"),
                    c = b.indexOf("-");
                0 === a && (a = b.indexOf("+", 1));
                0 === c && (c = b.indexOf("-", 1));
                var d = b.substring(b.length - 1, b.length);
                d = "i" === d || "j" === d;
                return 0 <= a || 0 <= c ? d ? 0 <= a ? isNaN(b.substring(0, a)) || isNaN(b.substring(a + 1, b.length - 1)) ? h.num : Number(b.substring(0,
                    a)) : isNaN(b.substring(0, c)) || isNaN(b.substring(c + 1, b.length - 1)) ? h.num : Number(b.substring(0, c)) : h.num : d ? isNaN(b.substring(0, b.length - 1)) ? h.num : 0 : isNaN(b) ? h.num : b
            };
            g.IMSEC = function(b) {
                if (!0 === b || !1 === b) return h.value;
                var a = g.IMREAL(b),
                    c = g.IMAGINARY(b);
                return e.anyIsError(a, c) ? h.value : g.IMDIV("1", g.IMCOS(b))
            };
            g.IMSECH = function(b) {
                var a = g.IMREAL(b),
                    c = g.IMAGINARY(b);
                return e.anyIsError(a, c) ? h.value : g.IMDIV("1", g.IMCOSH(b))
            };
            g.IMSIN = function(b) {
                var a = g.IMREAL(b),
                    c = g.IMAGINARY(b);
                if (e.anyIsError(a, c)) return h.value;
                b = b.substring(b.length - 1);
                return g.COMPLEX(Math.sin(a) * (Math.exp(c) + Math.exp(-c)) / 2, Math.cos(a) * (Math.exp(c) - Math.exp(-c)) / 2, "i" === b || "j" === b ? b : "i")
            };
            g.IMSINH = function(b) {
                var a = g.IMREAL(b),
                    c = g.IMAGINARY(b);
                if (e.anyIsError(a, c)) return h.value;
                b = b.substring(b.length - 1);
                return g.COMPLEX(Math.cos(c) * (Math.exp(a) - Math.exp(-a)) / 2, Math.sin(c) * (Math.exp(a) + Math.exp(-a)) / 2, "i" === b || "j" === b ? b : "i")
            };
            g.IMSQRT = function(b) {
                var a = g.IMREAL(b),
                    c = g.IMAGINARY(b);
                if (e.anyIsError(a, c)) return h.value;
                a = b.substring(b.length -
                    1);
                a = "i" === a || "j" === a ? a : "i";
                c = Math.sqrt(g.IMABS(b));
                b = g.IMARGUMENT(b);
                return g.COMPLEX(c * Math.cos(b / 2), c * Math.sin(b / 2), a)
            };
            g.IMCSC = function(b) {
                if (!0 === b || !1 === b) return h.value;
                var a = g.IMREAL(b),
                    c = g.IMAGINARY(b);
                return e.anyIsError(a, c) ? h.num : g.IMDIV("1", g.IMSIN(b))
            };
            g.IMCSCH = function(b) {
                if (!0 === b || !1 === b) return h.value;
                var a = g.IMREAL(b),
                    c = g.IMAGINARY(b);
                return e.anyIsError(a, c) ? h.num : g.IMDIV("1", g.IMSINH(b))
            };
            g.IMSUB = function(b, a) {
                var c = this.IMREAL(b),
                    d = this.IMAGINARY(b),
                    f = this.IMREAL(a),
                    k = this.IMAGINARY(a);
                if (e.anyIsError(c, d, f, k)) return h.value;
                b = b.substring(b.length - 1);
                a = a.substring(a.length - 1);
                var l = "i";
                "j" === b ? l = "j" : "j" === a && (l = "j");
                return this.COMPLEX(c - f, d - k, l)
            };
            g.IMSUM = function() {
                for (var b = e.flatten(arguments), a = b[0], c = 1; c < b.length; c++) {
                    var d = this.IMREAL(a);
                    a = this.IMAGINARY(a);
                    var f = this.IMREAL(b[c]),
                        k = this.IMAGINARY(b[c]);
                    if (e.anyIsError(d, a, f, k)) return h.value;
                    a = this.COMPLEX(d + f, a + k)
                }
                return a
            };
            g.IMTAN = function(b) {
                if (!0 === b || !1 === b) return h.value;
                var a = g.IMREAL(b),
                    c = g.IMAGINARY(b);
                return e.anyIsError(a,
                    c) ? h.value : this.IMDIV(this.IMSIN(b), this.IMCOS(b))
            };
            g.OCT2BIN = function(b, a) {
                if (!/^[0-7]{1,10}$/.test(b)) return h.num;
                var c = 10 === b.length && "7" === b.substring(0, 1) ? !0 : !1;
                b = c ? parseInt(b, 8) - 1073741824 : parseInt(b, 8);
                if (-512 > b || 511 < b) return h.num;
                if (c) return "1" + REPT("0", 9 - (512 + b).toString(2).length) + (512 + b).toString(2);
                c = b.toString(2);
                if ("undefined" === typeof a) return c;
                if (isNaN(a)) return h.value;
                if (0 > a) return h.num;
                a = Math.floor(a);
                return a >= c.length ? REPT("0", a - c.length) + c : h.num
            };
            g.OCT2DEC = function(b) {
                if (!/^[0-7]{1,10}$/.test(b)) return h.num;
                b = parseInt(b, 8);
                return 536870912 <= b ? b - 1073741824 : b
            };
            g.OCT2HEX = function(b, a) {
                if (!/^[0-7]{1,10}$/.test(b)) return h.num;
                b = parseInt(b, 8);
                if (536870912 <= b) return "ff" + (b + 3221225472).toString(16);
                b = b.toString(16);
                if (void 0 === a) return b;
                if (isNaN(a)) return h.value;
                if (0 > a) return h.num;
                a = Math.floor(a);
                return a >= b.length ? REPT("0", a - b.length) + b : h.num
            };
            return g
        }();
        t.financial = function() {
            function g(c) {
                return c && c.getTime && !isNaN(c.getTime())
            }

            function b(c) {
                return c instanceof Date ? c : new Date(c)
            }
            var a = {
                ACCRINT: function(c,
                    d, f, k, l, m, n) {
                    c = b(c);
                    d = b(d);
                    f = b(f);
                    return g(c) && g(d) && g(f) ? 0 >= k || 0 >= l || -1 === [1, 2, 4].indexOf(m) || -1 === [0, 1, 2, 3, 4].indexOf(n) || f <= c ? "#NUM!" : (l || 0) * k * YEARFRAC(c, f, n || 0) : "#VALUE!"
                },
                ACCRINTM: null,
                AMORDEGRC: null,
                AMORLINC: null,
                COUPDAYBS: null,
                COUPDAYS: null,
                COUPDAYSNC: null,
                COUPNCD: null,
                COUPNUM: null,
                COUPPCD: null,
                CUMIPMT: function(c, d, f, k, l, m) {
                    c = e.parseNumber(c);
                    d = e.parseNumber(d);
                    f = e.parseNumber(f);
                    if (e.anyIsError(c, d, f)) return h.value;
                    if (0 >= c || 0 >= d || 0 >= f || 1 > k || 1 > l || k > l || 0 !== m && 1 !== m) return h.num;
                    d = a.PMT(c, d,
                        f, 0, m);
                    var n = 0;
                    1 === k && 0 === m && (n = -f, k++);
                    for (; k <= l; k++) n = 1 === m ? n + (a.FV(c, k - 2, d, f, 1) - d) : n + a.FV(c, k - 1, d, f, 0);
                    return n * c
                },
                CUMPRINC: function(c, d, f, k, l, m) {
                    c = e.parseNumber(c);
                    d = e.parseNumber(d);
                    f = e.parseNumber(f);
                    if (e.anyIsError(c, d, f)) return h.value;
                    if (0 >= c || 0 >= d || 0 >= f || 1 > k || 1 > l || k > l || 0 !== m && 1 !== m) return h.num;
                    d = a.PMT(c, d, f, 0, m);
                    var n = 0;
                    1 === k && (n = 0 === m ? d + f * c : d, k++);
                    for (; k <= l; k++) n = 0 < m ? n + (d - (a.FV(c, k - 2, d, f, 1) - d) * c) : n + (d - a.FV(c, k - 1, d, f, 0) * c);
                    return n
                },
                DB: function(c, d, f, k, l) {
                    l = void 0 === l ? 12 : l;
                    c = e.parseNumber(c);
                    d = e.parseNumber(d);
                    f = e.parseNumber(f);
                    k = e.parseNumber(k);
                    l = e.parseNumber(l);
                    if (e.anyIsError(c, d, f, k, l)) return h.value;
                    if (0 > c || 0 > d || 0 > f || 0 > k || -1 === [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12].indexOf(l) || k > f) return h.num;
                    if (d >= c) return 0;
                    d = (1 - Math.pow(d / c, 1 / f)).toFixed(3);
                    for (var m = l = c * d * l / 12, n = 0, r = k === f ? f - 1 : k, u = 2; u <= r; u++) n = (c - m) * d, m += n;
                    return 1 === k ? l : k === f ? (c - m) * d : n
                },
                DDB: function(c, d, f, k, l) {
                    l = void 0 === l ? 2 : l;
                    c = e.parseNumber(c);
                    d = e.parseNumber(d);
                    f = e.parseNumber(f);
                    k = e.parseNumber(k);
                    l = e.parseNumber(l);
                    if (e.anyIsError(c,
                            d, f, k, l)) return h.value;
                    if (0 > c || 0 > d || 0 > f || 0 > k || 0 >= l || k > f) return h.num;
                    if (d >= c) return 0;
                    for (var m = 0, n = 0, r = 1; r <= k; r++) n = Math.min(l / f * (c - m), c - d - m), m += n;
                    return n
                },
                DISC: null,
                DOLLARDE: function(c, d) {
                    c = e.parseNumber(c);
                    d = e.parseNumber(d);
                    if (e.anyIsError(c, d)) return h.value;
                    if (0 > d) return h.num;
                    if (0 <= d && 1 > d) return h.div0;
                    d = parseInt(d, 10);
                    var f = parseInt(c, 10);
                    f += c % 1 * Math.pow(10, Math.ceil(Math.log(d) / Math.LN10)) / d;
                    c = Math.pow(10, Math.ceil(Math.log(d) / Math.LN2) + 1);
                    return f = Math.round(f * c) / c
                },
                DOLLARFR: function(c,
                    d) {
                    c = e.parseNumber(c);
                    d = e.parseNumber(d);
                    if (e.anyIsError(c, d)) return h.value;
                    if (0 > d) return h.num;
                    if (0 <= d && 1 > d) return h.div0;
                    d = parseInt(d, 10);
                    var f = parseInt(c, 10);
                    return f += c % 1 * Math.pow(10, -Math.ceil(Math.log(d) / Math.LN10)) * d
                },
                DURATION: null,
                EFFECT: function(c, d) {
                    c = e.parseNumber(c);
                    d = e.parseNumber(d);
                    if (e.anyIsError(c, d)) return h.value;
                    if (0 >= c || 1 > d) return h.num;
                    d = parseInt(d, 10);
                    return Math.pow(1 + c / d, d) - 1
                },
                FV: function(c, d, f, k, l) {
                    k = k || 0;
                    l = l || 0;
                    c = e.parseNumber(c);
                    d = e.parseNumber(d);
                    f = e.parseNumber(f);
                    k =
                        e.parseNumber(k);
                    l = e.parseNumber(l);
                    if (e.anyIsError(c, d, f, k, l)) return h.value;
                    0 === c ? c = k + f * d : (d = Math.pow(1 + c, d), c = 1 === l ? k * d + f * (1 + c) * (d - 1) / c : k * d + f * (d - 1) / c);
                    return -c
                },
                FVSCHEDULE: function(c, d) {
                    c = e.parseNumber(c);
                    d = e.parseNumberArray(e.flatten(d));
                    if (e.anyIsError(c, d)) return h.value;
                    for (var f = d.length, k = 0; k < f; k++) c *= 1 + d[k];
                    return c
                },
                INTRATE: null,
                IPMT: function(c, d, f, k, l, m) {
                    l = l || 0;
                    m = m || 0;
                    c = e.parseNumber(c);
                    d = e.parseNumber(d);
                    f = e.parseNumber(f);
                    k = e.parseNumber(k);
                    l = e.parseNumber(l);
                    m = e.parseNumber(m);
                    if (e.anyIsError(c,
                            d, f, k, l, m)) return h.value;
                    f = a.PMT(c, f, k, l, m);
                    return (1 === d ? 1 === m ? 0 : -k : 1 === m ? a.FV(c, d - 2, f, k, 1) - f : a.FV(c, d - 1, f, k, 0)) * c
                },
                IRR: function(c, d) {
                    d = d || 0;
                    c = e.parseNumberArray(e.flatten(c));
                    d = e.parseNumber(d);
                    if (e.anyIsError(c, d)) return h.value;
                    for (var f = function(u, w, x) {
                            x += 1;
                            for (var v = u[0], y = 1; y < u.length; y++) v += u[y] / Math.pow(x, (w[y] - w[0]) / 365);
                            return v
                        }, k = function(u, w, x) {
                            x += 1;
                            for (var v = 0, y = 1; y < u.length; y++) {
                                var E = (w[y] - w[0]) / 365;
                                v -= E * u[y] / Math.pow(x, E + 1)
                            }
                            return v
                        }, l = [], m = !1, n = !1, r = 0; r < c.length; r++) l[r] = 0 === r ?
                        0 : l[r - 1] + 365, 0 < c[r] && (m = !0), 0 > c[r] && (n = !0);
                    if (!m || !n) return h.num;
                    d = void 0 === d ? .1 : d;
                    m = !0;
                    do r = f(c, l, d), m = d - r / k(c, l, d), n = Math.abs(m - d), d = m, m = 1E-10 < n && 1E-10 < Math.abs(r); while (m);
                    return d
                },
                ISPMT: function(c, d, f, k) {
                    c = e.parseNumber(c);
                    d = e.parseNumber(d);
                    f = e.parseNumber(f);
                    k = e.parseNumber(k);
                    return e.anyIsError(c, d, f, k) ? h.value : k * c * (d / f - 1)
                },
                MDURATION: null,
                MIRR: function(c, d, f) {
                    c = e.parseNumberArray(e.flatten(c));
                    d = e.parseNumber(d);
                    f = e.parseNumber(f);
                    if (e.anyIsError(c, d, f)) return h.value;
                    for (var k = c.length,
                            l = [], m = [], n = 0; n < k; n++) 0 > c[n] ? l.push(c[n]) : m.push(c[n]);
                    c = -a.NPV(f, m) * Math.pow(1 + f, k - 1);
                    d = a.NPV(d, l) * (1 + d);
                    return Math.pow(c / d, 1 / (k - 1)) - 1
                },
                NOMINAL: function(c, d) {
                    c = e.parseNumber(c);
                    d = e.parseNumber(d);
                    if (e.anyIsError(c, d)) return h.value;
                    if (0 >= c || 1 > d) return h.num;
                    d = parseInt(d, 10);
                    return (Math.pow(c + 1, 1 / d) - 1) * d
                },
                NPER: function(c, d, f, k, l) {
                    l = void 0 === l ? 0 : l;
                    k = void 0 === k ? 0 : k;
                    c = e.parseNumber(c);
                    d = e.parseNumber(d);
                    f = e.parseNumber(f);
                    k = e.parseNumber(k);
                    l = e.parseNumber(l);
                    return e.anyIsError(c, d, f, k, l) ? h.value :
                        Math.log((d * (1 + c * l) - k * c) / (f * c + d * (1 + c * l))) / Math.log(1 + c)
                },
                NPV: function() {
                    var c = e.parseNumberArray(e.flatten(arguments));
                    if (c instanceof Error) return c;
                    for (var d = c[0], f = 0, k = 1; k < c.length; k++) f += c[k] / Math.pow(1 + d, k);
                    return f
                },
                ODDFPRICE: null,
                ODDFYIELD: null,
                ODDLPRICE: null,
                ODDLYIELD: null,
                PDURATION: function(c, d, f) {
                    c = e.parseNumber(c);
                    d = e.parseNumber(d);
                    f = e.parseNumber(f);
                    return e.anyIsError(c, d, f) ? h.value : 0 >= c ? h.num : (Math.log(f) - Math.log(d)) / Math.log(1 + c)
                },
                PMT: function(c, d, f, k, l) {
                    k = k || 0;
                    l = l || 0;
                    c = e.parseNumber(c);
                    d = e.parseNumber(d);
                    f = e.parseNumber(f);
                    k = e.parseNumber(k);
                    l = e.parseNumber(l);
                    if (e.anyIsError(c, d, f, k, l)) return h.value;
                    0 === c ? c = (f + k) / d : (d = Math.pow(1 + c, d), c = 1 === l ? (k * c / (d - 1) + f * c / (1 - 1 / d)) / (1 + c) : k * c / (d - 1) + f * c / (1 - 1 / d));
                    return -c
                },
                PPMT: function(c, d, f, k, l, m) {
                    l = l || 0;
                    m = m || 0;
                    c = e.parseNumber(c);
                    f = e.parseNumber(f);
                    k = e.parseNumber(k);
                    l = e.parseNumber(l);
                    m = e.parseNumber(m);
                    return e.anyIsError(c, f, k, l, m) ? h.value : a.PMT(c, f, k, l, m) - a.IPMT(c, d, f, k, l, m)
                },
                PRICE: null,
                PRICEDISC: null,
                PRICEMAT: null,
                PV: function(c, d, f, k, l) {
                    k =
                        k || 0;
                    l = l || 0;
                    c = e.parseNumber(c);
                    d = e.parseNumber(d);
                    f = e.parseNumber(f);
                    k = e.parseNumber(k);
                    l = e.parseNumber(l);
                    return e.anyIsError(c, d, f, k, l) ? h.value : 0 === c ? -f * d - k : ((1 - Math.pow(1 + c, d)) / c * f * (1 + c * l) - k) / Math.pow(1 + c, d)
                },
                RATE: function(c, d, f, k, l, m) {
                    m = void 0 === m ? .01 : m;
                    k = void 0 === k ? 0 : k;
                    l = void 0 === l ? 0 : l;
                    c = e.parseNumber(c);
                    d = e.parseNumber(d);
                    f = e.parseNumber(f);
                    k = e.parseNumber(k);
                    l = e.parseNumber(l);
                    m = e.parseNumber(m);
                    if (e.anyIsError(c, d, f, k, l, m)) return h.value;
                    for (var n = 0, r = !1; 100 > n && !r;) {
                        var u = Math.pow(m + 1, c),
                            w = Math.pow(m + 1, c - 1);
                        u = m - (k + u * f + d * (u - 1) * (m * l + 1) / m) / (c * w * f - d * (u - 1) * (m * l + 1) / Math.pow(m, 2) + (c * d * w * (m * l + 1) / m + d * (u - 1) * l / m));
                        1E-6 > Math.abs(u - m) && (r = !0);
                        n++;
                        m = u
                    }
                    return r ? m : Number.NaN + m
                },
                RECEIVED: null,
                RRI: function(c, d, f) {
                    c = e.parseNumber(c);
                    d = e.parseNumber(d);
                    f = e.parseNumber(f);
                    return e.anyIsError(c, d, f) ? h.value : 0 === c || 0 === d ? h.num : Math.pow(f / d, 1 / c) - 1
                },
                SLN: function(c, d, f) {
                    c = e.parseNumber(c);
                    d = e.parseNumber(d);
                    f = e.parseNumber(f);
                    return e.anyIsError(c, d, f) ? h.value : 0 === f ? h.num : (c - d) / f
                },
                SYD: function(c, d, f, k) {
                    c =
                        e.parseNumber(c);
                    d = e.parseNumber(d);
                    f = e.parseNumber(f);
                    k = e.parseNumber(k);
                    if (e.anyIsError(c, d, f, k)) return h.value;
                    if (0 === f || 1 > k || k > f) return h.num;
                    k = parseInt(k, 10);
                    return (c - d) * (f - k + 1) * 2 / (f * (f + 1))
                },
                TBILLEQ: function(c, d, f) {
                    c = e.parseDate(c);
                    d = e.parseDate(d);
                    f = e.parseNumber(f);
                    return e.anyIsError(c, d, f) ? h.value : 0 >= f || c > d || 31536E6 < d - c ? h.num : 365 * f / (360 - f * DAYS360(c, d, !1))
                },
                TBILLPRICE: function(c, d, f) {
                    c = e.parseDate(c);
                    d = e.parseDate(d);
                    f = e.parseNumber(f);
                    return e.anyIsError(c, d, f) ? h.value : 0 >= f || c > d || 31536E6 <
                        d - c ? h.num : 100 * (1 - f * DAYS360(c, d, !1) / 360)
                },
                TBILLYIELD: function(c, d, f) {
                    c = e.parseDate(c);
                    d = e.parseDate(d);
                    f = e.parseNumber(f);
                    return e.anyIsError(c, d, f) ? h.value : 0 >= f || c > d || 31536E6 < d - c ? h.num : 360 * (100 - f) / (f * DAYS360(c, d, !1))
                },
                VDB: null,
                XIRR: function(c, d, f) {
                    c = e.parseNumberArray(e.flatten(c));
                    d = e.parseDateArray(e.flatten(d));
                    f = e.parseNumber(f);
                    if (e.anyIsError(c, d, f)) return h.value;
                    for (var k = function(u, w, x) {
                            x += 1;
                            for (var v = u[0], y = 1; y < u.length; y++) v += u[y] / Math.pow(x, DAYS(w[y], w[0]) / 365);
                            return v
                        }, l = function(u,
                            w, x) {
                            x += 1;
                            for (var v = 0, y = 1; y < u.length; y++) {
                                var E = DAYS(w[y], w[0]) / 365;
                                v -= E * u[y] / Math.pow(x, E + 1)
                            }
                            return v
                        }, m = !1, n = !1, r = 0; r < c.length; r++) 0 < c[r] && (m = !0), 0 > c[r] && (n = !0);
                    if (!m || !n) return h.num;
                    f = f || .1;
                    m = !0;
                    do r = k(c, d, f), m = f - r / l(c, d, f), n = Math.abs(m - f), f = m, m = 1E-10 < n && 1E-10 < Math.abs(r); while (m);
                    return f
                },
                XNPV: function(c, d, f) {
                    c = e.parseNumber(c);
                    d = e.parseNumberArray(e.flatten(d));
                    f = e.parseDateArray(e.flatten(f));
                    if (e.anyIsError(c, d, f)) return h.value;
                    for (var k = 0, l = 0; l < d.length; l++) k += d[l] / Math.pow(1 + c, DAYS(f[l],
                        f[0]) / 365);
                    return k
                },
                YIELD: null,
                YIELDDISC: null,
                YIELDMAT: null
            };
            return a
        }();
        t.information = function() {
            var g = {
                CELL: null,
                ERROR: {}
            };
            g.ERROR.TYPE = function(b) {
                switch (b) {
                    case h.nil:
                        return 1;
                    case h.div0:
                        return 2;
                    case h.value:
                        return 3;
                    case h.ref:
                        return 4;
                    case h.name:
                        return 5;
                    case h.num:
                        return 6;
                    case h.na:
                        return 7;
                    case h.data:
                        return 8
                }
                return h.na
            };
            g.INFO = null;
            g.ISBLANK = function(b) {
                return null === b
            };
            g.ISBINARY = function(b) {
                return /^[01]{1,10}$/.test(b)
            };
            g.ISERR = function(b) {
                return 0 <= [h.value, h.ref, h.div0, h.num, h.name,
                    h.nil
                ].indexOf(b) || "number" === typeof b && (isNaN(b) || !isFinite(b))
            };
            g.ISERROR = function(b) {
                return g.ISERR(b) || b === h.na
            };
            g.ISEVEN = function(b) {
                return Math.floor(Math.abs(b)) & 1 ? !1 : !0
            };
            g.ISFORMULA = null;
            g.ISLOGICAL = function(b) {
                return !0 === b || !1 === b
            };
            g.ISNA = function(b) {
                return b === h.na
            };
            g.ISNONTEXT = function(b) {
                return "string" !== typeof b
            };
            g.ISNUMBER = function(b) {
                return "number" === typeof b && !isNaN(b) && isFinite(b)
            };
            g.ISODD = function(b) {
                return Math.floor(Math.abs(b)) & 1 ? !0 : !1
            };
            g.ISREF = null;
            g.ISTEXT = function(b) {
                return "string" ===
                    typeof b
            };
            g.N = function(b) {
                return this.ISNUMBER(b) ? b : b instanceof Date ? b.getTime() : !0 === b ? 1 : !1 === b ? 0 : this.ISERROR(b) ? b : 0
            };
            g.NA = function() {
                return h.na
            };
            g.SHEET = null;
            g.SHEETS = null;
            g.TYPE = function(b) {
                if (this.ISNUMBER(b)) return 1;
                if (this.ISTEXT(b)) return 2;
                if (this.ISLOGICAL(b)) return 4;
                if (this.ISERROR(b)) return 16;
                if (Array.isArray(b)) return 64
            };
            return g
        }();
        t.logical = function() {
            return {
                AND: function() {
                    for (var g = e.flatten(arguments), b = !0, a = 0; a < g.length; a++) g[a] || (b = !1);
                    return b
                },
                CHOOSE: function() {
                    if (2 > arguments.length) return h.na;
                    var g = arguments[0];
                    return 1 > g || 254 < g || arguments.length < g + 1 ? h.value : arguments[g]
                },
                FALSE: function() {
                    return !1
                },
                IF: function(g, b, a) {
                    return g ? b : a
                },
                IFERROR: function(g, b) {
                    return ISERROR(g) ? b : g
                },
                IFNA: function(g, b) {
                    return g === h.na ? b : g
                },
                NOT: function(g) {
                    return !g
                },
                OR: function() {
                    for (var g = e.flatten(arguments), b = !1, a = 0; a < g.length; a++) g[a] && (b = !0);
                    return b
                },
                TRUE: function() {
                    return !0
                },
                XOR: function() {
                    for (var g = e.flatten(arguments), b = 0, a = 0; a < g.length; a++) g[a] && b++;
                    return Math.floor(Math.abs(b)) & 1 ? !0 : !1
                },
                SWITCH: function() {
                    if (0 <
                        arguments.length) {
                        var g = arguments[0],
                            b = arguments.length - 1,
                            a = Math.floor(b / 2),
                            c = !1;
                        b = 0 === b % 2 ? null : arguments[arguments.length - 1];
                        if (a)
                            for (var d = 0; d < a; d++)
                                if (g === arguments[2 * d + 1]) {
                                    var f = arguments[2 * d + 2];
                                    c = !0;
                                    break
                                }! c && b && (f = b)
                    }
                    return f
                }
            }
        }();
        t.math = function() {
            var g = {
                ABS: function(a) {
                    a = e.parseNumber(a);
                    return a instanceof Error ? a : Math.abs(e.parseNumber(a))
                },
                ACOS: function(a) {
                    a = e.parseNumber(a);
                    return a instanceof Error ? a : Math.acos(a)
                },
                ACOSH: function(a) {
                    a = e.parseNumber(a);
                    return a instanceof Error ? a : Math.log(a +
                        Math.sqrt(a * a - 1))
                },
                ACOT: function(a) {
                    a = e.parseNumber(a);
                    return a instanceof Error ? a : Math.atan(1 / a)
                },
                ACOTH: function(a) {
                    a = e.parseNumber(a);
                    return a instanceof Error ? a : .5 * Math.log((a + 1) / (a - 1))
                },
                AGGREGATE: null,
                ARABIC: function(a) {
                    if (!/^M*(?:D?C{0,3}|C[MD])(?:L?X{0,3}|X[CL])(?:V?I{0,3}|I[XV])$/.test(a)) return h.value;
                    var c = 0;
                    a.replace(/[MDLV]|C[MD]?|X[CL]?|I[XV]?/g, function(d) {
                        c += {
                            M: 1E3,
                            CM: 900,
                            D: 500,
                            CD: 400,
                            C: 100,
                            XC: 90,
                            L: 50,
                            XL: 40,
                            X: 10,
                            IX: 9,
                            V: 5,
                            IV: 4,
                            I: 1
                        } [d]
                    });
                    return c
                },
                ASIN: function(a) {
                    a = e.parseNumber(a);
                    return a instanceof Error ? a : Math.asin(a)
                },
                ASINH: function(a) {
                    a = e.parseNumber(a);
                    return a instanceof Error ? a : Math.log(a + Math.sqrt(a * a + 1))
                },
                ATAN: function(a) {
                    a = e.parseNumber(a);
                    return a instanceof Error ? a : Math.atan(a)
                },
                ATAN2: function(a, c) {
                    a = e.parseNumber(a);
                    c = e.parseNumber(c);
                    return e.anyIsError(a, c) ? h.value : Math.atan2(a, c)
                },
                ATANH: function(a) {
                    a = e.parseNumber(a);
                    return a instanceof Error ? a : Math.log((1 + a) / (1 - a)) / 2
                },
                BASE: function(a, c, d) {
                    d = d || 0;
                    a = e.parseNumber(a);
                    c = e.parseNumber(c);
                    d = e.parseNumber(d);
                    if (e.anyIsError(a,
                            c, d)) return h.value;
                    d = void 0 === d ? 0 : d;
                    a = a.toString(c);
                    return Array(Math.max(d + 1 - a.length, 0)).join("0") + a
                },
                CEILING: function(a, c, d) {
                    c = void 0 === c ? 1 : c;
                    d = void 0 === d ? 0 : d;
                    a = e.parseNumber(a);
                    c = e.parseNumber(c);
                    d = e.parseNumber(d);
                    if (e.anyIsError(a, c, d)) return h.value;
                    if (0 === c) return 0;
                    c = Math.abs(c);
                    return 0 <= a ? Math.ceil(a / c) * c : 0 === d ? -1 * Math.floor(Math.abs(a) / c) * c : -1 * Math.ceil(Math.abs(a) / c) * c
                }
            };
            g.CEILING.MATH = g.CEILING;
            g.CEILING.PRECISE = g.CEILING;
            g.COMBIN = function(a, c) {
                a = e.parseNumber(a);
                c = e.parseNumber(c);
                return e.anyIsError(a, c) ? h.value : g.FACT(a) / (g.FACT(c) * g.FACT(a - c))
            };
            g.COMBINA = function(a, c) {
                a = e.parseNumber(a);
                c = e.parseNumber(c);
                return e.anyIsError(a, c) ? h.value : 0 === a && 0 === c ? 1 : g.COMBIN(a + c - 1, a - 1)
            };
            g.COS = function(a) {
                a = e.parseNumber(a);
                return a instanceof Error ? a : Math.cos(a)
            };
            g.COSH = function(a) {
                a = e.parseNumber(a);
                return a instanceof Error ? a : (Math.exp(a) + Math.exp(-a)) / 2
            };
            g.COT = function(a) {
                a = e.parseNumber(a);
                return a instanceof Error ? a : 1 / Math.tan(a)
            };
            g.COTH = function(a) {
                a = e.parseNumber(a);
                if (a instanceof Error) return a;
                a = Math.exp(2 * a);
                return (a + 1) / (a - 1)
            };
            g.CSC = function(a) {
                a = e.parseNumber(a);
                return a instanceof Error ? a : 1 / Math.sin(a)
            };
            g.CSCH = function(a) {
                a = e.parseNumber(a);
                return a instanceof Error ? a : 2 / (Math.exp(a) - Math.exp(-a))
            };
            g.DECIMAL = function(a, c) {
                return 1 > arguments.length ? h.value : parseInt(a, c)
            };
            g.DEGREES = function(a) {
                a = e.parseNumber(a);
                return a instanceof Error ? a : 180 * a / Math.PI
            };
            g.EVEN = function(a) {
                a = e.parseNumber(a);
                return a instanceof Error ? a : g.CEILING(a, -2, -1)
            };
            g.EXP = Math.exp;
            var b = [];
            g.FACT = function(a) {
                a =
                    e.parseNumber(a);
                if (a instanceof Error) return a;
                a = Math.floor(a);
                if (0 === a || 1 === a) return 1;
                0 < b[a] || (b[a] = g.FACT(a - 1) * a);
                return b[a]
            };
            g.FACTDOUBLE = function(a) {
                a = e.parseNumber(a);
                if (a instanceof Error) return a;
                a = Math.floor(a);
                return 0 >= a ? 1 : a * g.FACTDOUBLE(a - 2)
            };
            g.FLOOR = function(a, c, d) {
                c = void 0 === c ? 1 : c;
                d = void 0 === d ? 0 : d;
                a = e.parseNumber(a);
                c = e.parseNumber(c);
                d = e.parseNumber(d);
                if (e.anyIsError(a, c, d)) return h.value;
                if (0 === c) return 0;
                c = Math.abs(c);
                return 0 <= a ? Math.floor(a / c) * c : 0 === d ? -1 * Math.ceil(Math.abs(a) /
                    c) * c : -1 * Math.floor(Math.abs(a) / c) * c
            };
            g.FLOOR.MATH = g.FLOOR;
            g.GCD = null;
            g.INT = function(a) {
                a = e.parseNumber(a);
                return a instanceof Error ? a : Math.floor(a)
            };
            g.LCM = function() {
                var a = e.parseNumberArray(e.flatten(arguments));
                if (a instanceof Error) return a;
                for (var c, d, f, k = 1; void 0 !== (f = a.pop());)
                    for (; 1 < f;) {
                        if (f % 2) {
                            c = 3;
                            for (d = Math.floor(Math.sqrt(f)); c <= d && f % c; c += 2);
                            d = c <= d ? c : f
                        } else d = 2;
                        f /= d;
                        k *= d;
                        for (c = a.length; c; 0 === a[--c] % d && 1 === (a[c] /= d) && a.splice(c, 1));
                    }
                return k
            };
            g.LN = function(a) {
                a = e.parseNumber(a);
                return a instanceof
                Error ? a : Math.log(a)
            };
            g.LOG = function(a, c) {
                a = e.parseNumber(a);
                c = void 0 === c ? 10 : e.parseNumber(c);
                return e.anyIsError(a, c) ? h.value : Math.log(a) / Math.log(c)
            };
            g.LOG10 = function(a) {
                a = e.parseNumber(a);
                return a instanceof Error ? a : Math.log(a) / Math.log(10)
            };
            g.MDETERM = null;
            g.MINVERSE = null;
            g.MMULT = null;
            g.MOD = function(a, c) {
                a = e.parseNumber(a);
                c = e.parseNumber(c);
                if (e.anyIsError(a, c)) return h.value;
                if (0 === c) return h.div0;
                a = Math.abs(a % c);
                return 0 < c ? a : -a
            };
            g.MROUND = function(a, c) {
                a = e.parseNumber(a);
                c = e.parseNumber(c);
                return e.anyIsError(a,
                    c) ? h.value : 0 > a * c ? h.num : Math.round(a / c) * c
            };
            g.MULTINOMIAL = function() {
                var a = e.parseNumberArray(e.flatten(arguments));
                if (a instanceof Error) return a;
                for (var c = 0, d = 1, f = 0; f < a.length; f++) c += a[f], d *= g.FACT(a[f]);
                return g.FACT(c) / d
            };
            g.MUNIT = null;
            g.ODD = function(a) {
                a = e.parseNumber(a);
                if (a instanceof Error) return a;
                var c = Math.ceil(Math.abs(a));
                c = c & 1 ? c : c + 1;
                return 0 < a ? c : -c
            };
            g.PI = function() {
                return Math.PI
            };
            g.POWER = function(a, c) {
                a = e.parseNumber(a);
                c = e.parseNumber(c);
                if (e.anyIsError(a, c)) return h.value;
                a = Math.pow(a,
                    c);
                return isNaN(a) ? h.num : a
            };
            g.PRODUCT = function() {
                var a = e.parseNumberArray(e.flatten(arguments));
                if (a instanceof Error) return a;
                for (var c = 1, d = 0; d < a.length; d++) c *= a[d];
                return c
            };
            g.QUOTIENT = function(a, c) {
                a = e.parseNumber(a);
                c = e.parseNumber(c);
                return e.anyIsError(a, c) ? h.value : parseInt(a / c, 10)
            };
            g.RADIANS = function(a) {
                a = e.parseNumber(a);
                return a instanceof Error ? a : a * Math.PI / 180
            };
            g.RAND = function() {
                return Math.random()
            };
            g.RANDBETWEEN = function(a, c) {
                a = e.parseNumber(a);
                c = e.parseNumber(c);
                return e.anyIsError(a,
                    c) ? h.value : a + Math.ceil((c - a + 1) * Math.random()) - 1
            };
            g.ROMAN = null;
            g.ROUND = function(a, c) {
                a = e.parseNumber(a);
                c = e.parseNumber(c);
                return e.anyIsError(a, c) ? h.value : Math.round(a * Math.pow(10, c)) / Math.pow(10, c)
            };
            g.ROUNDDOWN = function(a, c) {
                a = e.parseNumber(a);
                c = e.parseNumber(c);
                return e.anyIsError(a, c) ? h.value : (0 < a ? 1 : -1) * Math.floor(Math.abs(a) * Math.pow(10, c)) / Math.pow(10, c)
            };
            g.ROUNDUP = function(a, c) {
                a = e.parseNumber(a);
                c = e.parseNumber(c);
                return e.anyIsError(a, c) ? h.value : (0 < a ? 1 : -1) * Math.ceil(Math.abs(a) * Math.pow(10,
                    c)) / Math.pow(10, c)
            };
            g.SEC = function(a) {
                a = e.parseNumber(a);
                return a instanceof Error ? a : 1 / Math.cos(a)
            };
            g.SECH = function(a) {
                a = e.parseNumber(a);
                return a instanceof Error ? a : 2 / (Math.exp(a) + Math.exp(-a))
            };
            g.SERIESSUM = function(a, c, d, f) {
                a = e.parseNumber(a);
                c = e.parseNumber(c);
                d = e.parseNumber(d);
                f = e.parseNumberArray(f);
                if (e.anyIsError(a, c, d, f)) return h.value;
                for (var k = f[0] * Math.pow(a, c), l = 1; l < f.length; l++) k += f[l] * Math.pow(a, c + l * d);
                return k
            };
            g.SIGN = function(a) {
                a = e.parseNumber(a);
                return a instanceof Error ? a : 0 > a ?
                    -1 : 0 === a ? 0 : 1
            };
            g.SIN = function(a) {
                a = e.parseNumber(a);
                return a instanceof Error ? a : Math.sin(a)
            };
            g.SINH = function(a) {
                a = e.parseNumber(a);
                return a instanceof Error ? a : (Math.exp(a) - Math.exp(-a)) / 2
            };
            g.SQRT = function(a) {
                a = e.parseNumber(a);
                return a instanceof Error ? a : 0 > a ? h.num : Math.sqrt(a)
            };
            g.SQRTPI = function(a) {
                a = e.parseNumber(a);
                return a instanceof Error ? a : Math.sqrt(a * Math.PI)
            };
            g.SUBTOTAL = null;
            g.ADD = function(a, c) {
                if (2 !== arguments.length) return h.na;
                a = e.parseNumber(a);
                c = e.parseNumber(c);
                return e.anyIsError(a, c) ?
                    h.value : a + c
            };
            g.MINUS = function(a, c) {
                if (2 !== arguments.length) return h.na;
                a = e.parseNumber(a);
                c = e.parseNumber(c);
                return e.anyIsError(a, c) ? h.value : a - c
            };
            g.DIVIDE = function(a, c) {
                if (2 !== arguments.length) return h.na;
                a = e.parseNumber(a);
                c = e.parseNumber(c);
                return e.anyIsError(a, c) ? h.value : 0 === c ? h.div0 : a / c
            };
            g.MULTIPLY = function(a, c) {
                if (2 !== arguments.length) return h.na;
                a = e.parseNumber(a);
                c = e.parseNumber(c);
                return e.anyIsError(a, c) ? h.value : a * c
            };
            g.GTE = function(a, c) {
                if (2 !== arguments.length) return h.na;
                a = e.parseNumber(a);
                c = e.parseNumber(c);
                return e.anyIsError(a, c) ? h.error : a >= c
            };
            g.LT = function(a, c) {
                if (2 !== arguments.length) return h.na;
                a = e.parseNumber(a);
                c = e.parseNumber(c);
                return e.anyIsError(a, c) ? h.error : a < c
            };
            g.LTE = function(a, c) {
                if (2 !== arguments.length) return h.na;
                a = e.parseNumber(a);
                c = e.parseNumber(c);
                return e.anyIsError(a, c) ? h.error : a <= c
            };
            g.EQ = function(a, c) {
                return 2 !== arguments.length ? h.na : a === c
            };
            g.NE = function(a, c) {
                return 2 !== arguments.length ? h.na : a !== c
            };
            g.POW = function(a, c) {
                if (2 !== arguments.length) return h.na;
                a = e.parseNumber(a);
                c = e.parseNumber(c);
                return e.anyIsError(a, c) ? h.error : g.POWER(a, c)
            };
            g.SUM = function() {
                for (var a = 0, c = Object.keys(arguments), d = 0; d < c.length; ++d) {
                    var f = arguments[c[d]];
                    "number" === typeof f ? a += f : "string" === typeof f ? (f = parseFloat(f), !isNaN(f) && (a += f)) : Array.isArray(f) && (a += g.SUM.apply(null, f))
                }
                return a
            };
            g.SUMIF = function() {
                var a = e.argsToArray(arguments),
                    c = a.pop();
                a = e.parseNumberArray(e.flatten(a));
                if (a instanceof Error) return a;
                for (var d = 0, f = 0; f < a.length; f++) d += eval(a[f] + c) ? a[f] : 0;
                return d
            };
            g.SUMIFS = function() {
                var a =
                    e.argsToArray(arguments),
                    c = e.parseNumberArray(e.flatten(a.shift()));
                if (c instanceof Error) return c;
                for (var d = c.length, f = a.length, k = 0, l = 0; l < d; l++) {
                    for (var m = c[l], n = "", r = 0; r < f; r += 2) n = isNaN(a[r][l]) ? n + ('"' + a[r][l] + '"' + a[r + 1]) : n + (a[r][l] + a[r + 1]), r !== f - 1 && (n += " && ");
                    n = n.slice(0, -4);
                    eval(n) && (k += m)
                }
                return k
            };
            g.SUMPRODUCT = null;
            g.SUMSQ = function() {
                var a = e.parseNumberArray(e.flatten(arguments));
                if (a instanceof Error) return a;
                for (var c = 0, d = a.length, f = 0; f < d; f++) c += ISNUMBER(a[f]) ? a[f] * a[f] : 0;
                return c
            };
            g.SUMX2MY2 =
                function(a, c) {
                    a = e.parseNumberArray(e.flatten(a));
                    c = e.parseNumberArray(e.flatten(c));
                    if (e.anyIsError(a, c)) return h.value;
                    for (var d = 0, f = 0; f < a.length; f++) d += a[f] * a[f] - c[f] * c[f];
                    return d
                };
            g.SUMX2PY2 = function(a, c) {
                a = e.parseNumberArray(e.flatten(a));
                c = e.parseNumberArray(e.flatten(c));
                if (e.anyIsError(a, c)) return h.value;
                var d = 0;
                a = e.parseNumberArray(e.flatten(a));
                c = e.parseNumberArray(e.flatten(c));
                for (var f = 0; f < a.length; f++) d += a[f] * a[f] + c[f] * c[f];
                return d
            };
            g.SUMXMY2 = function(a, c) {
                a = e.parseNumberArray(e.flatten(a));
                c = e.parseNumberArray(e.flatten(c));
                if (e.anyIsError(a, c)) return h.value;
                var d = 0;
                a = e.flatten(a);
                c = e.flatten(c);
                for (var f = 0; f < a.length; f++) d += Math.pow(a[f] - c[f], 2);
                return d
            };
            g.TAN = function(a) {
                a = e.parseNumber(a);
                return a instanceof Error ? a : Math.tan(a)
            };
            g.TANH = function(a) {
                a = e.parseNumber(a);
                if (a instanceof Error) return a;
                a = Math.exp(2 * a);
                return (a - 1) / (a + 1)
            };
            g.TRUNC = function(a, c) {
                c = void 0 === c ? 0 : c;
                a = e.parseNumber(a);
                c = e.parseNumber(c);
                return e.anyIsError(a, c) ? h.value : (0 < a ? 1 : -1) * Math.floor(Math.abs(a) * Math.pow(10,
                    c)) / Math.pow(10, c)
            };
            return g
        }();
        t.misc = function() {
            var g = {
                UNIQUE: function() {
                    for (var b = [], a = 0; a < arguments.length; ++a) {
                        for (var c = !1, d = arguments[a], f = 0; f < b.length && !(c = b[f] === d); ++f);
                        c || b.push(d)
                    }
                    return b
                }
            };
            g.FLATTEN = e.flatten;
            g.ARGS2ARRAY = function() {
                return Array.prototype.slice.call(arguments, 0)
            };
            g.REFERENCE = function(b, a) {
                try {
                    var c = a.split(".");
                    for (a = 0; a < c.length; ++a) {
                        var d = c[a];
                        if ("]" === d[d.length - 1]) {
                            var f = d.indexOf("["),
                                k = d.substring(f + 1, d.length - 1);
                            b = b[d.substring(0, f)][k]
                        } else b = b[d]
                    }
                    return b
                } catch (l) {}
            };
            g.JOIN = function(b, a) {
                return b.join(a)
            };
            g.NUMBERS = function() {
                return e.flatten(arguments).filter(function(b) {
                    return "number" === typeof b
                })
            };
            g.NUMERAL = null;
            return g
        }();
        t.text = function() {
            var g = {
                ASC: null,
                BAHTTEXT: null,
                CHAR: function(b) {
                    b = e.parseNumber(b);
                    return b instanceof Error ? b : String.fromCharCode(b)
                },
                CLEAN: function(b) {
                    return (b || "").replace(/[\0-\x1F]/g, "")
                },
                CODE: function(b) {
                    return (b || "").charCodeAt(0)
                },
                CONCATENATE: function() {
                    for (var b = e.flatten(arguments), a; - 1 < (a = b.indexOf(!0));) b[a] = "TRUE";
                    for (; - 1 <
                        (a = b.indexOf(!1));) b[a] = "FALSE";
                    return b.join("")
                },
                DBCS: null,
                DOLLAR: null,
                EXACT: function(b, a) {
                    return b === a
                },
                FIND: function(b, a, c) {
                    return a ? a.indexOf(b, (void 0 === c ? 0 : c) - 1) + 1 : null
                },
                FIXED: null,
                HTML2TEXT: function(b) {
                    var a = "";
                    b && (b instanceof Array ? b.forEach(function(c) {
                        "" !== a && (a += "\n");
                        a += c.replace(/<(?:.|\n)*?>/gm, "")
                    }) : a = b.replace(/<(?:.|\n)*?>/gm, ""));
                    return a
                },
                LEFT: function(b, a) {
                    a = e.parseNumber(void 0 === a ? 1 : a);
                    return a instanceof Error || "string" !== typeof b ? h.value : b ? b.substring(0, a) : null
                },
                LEN: function(b) {
                    return 0 ===
                        arguments.length ? h.error : "string" === typeof b ? b ? b.length : 0 : b.length ? b.length : h.value
                },
                LOWER: function(b) {
                    return "string" !== typeof b ? h.value : b ? b.toLowerCase() : b
                },
                MID: function(b, a, c) {
                    a = e.parseNumber(a);
                    c = e.parseNumber(c);
                    if (e.anyIsError(a, c) || "string" !== typeof b) return c;
                    --a;
                    return b.substring(a, a + c)
                },
                NUMBERVALUE: null,
                PRONETIC: null,
                PROPER: function(b) {
                    if (void 0 === b || 0 === b.length) return h.value;
                    !0 === b && (b = "TRUE");
                    !1 === b && (b = "FALSE");
                    if (isNaN(b) && "number" === typeof b) return h.value;
                    "number" === typeof b && (b =
                        "" + b);
                    return b.replace(/\w\S*/g, function(a) {
                        return a.charAt(0).toUpperCase() + a.substr(1).toLowerCase()
                    })
                },
                REGEXEXTRACT: function(b, a) {
                    return (b = b.match(new RegExp(a))) ? b[1 < b.length ? b.length - 1 : 0] : null
                },
                REGEXMATCH: function(b, a, c) {
                    b = b.match(new RegExp(a));
                    return c ? b : !!b
                },
                REGEXREPLACE: function(b, a, c) {
                    return b.replace(new RegExp(a), c)
                },
                REPLACE: function(b, a, c, d) {
                    a = e.parseNumber(a);
                    c = e.parseNumber(c);
                    return e.anyIsError(a, c) || "string" !== typeof b || "string" !== typeof d ? h.value : b.substr(0, a - 1) + d + b.substr(a - 1 +
                        c)
                },
                REPT: function(b, a) {
                    a = e.parseNumber(a);
                    return a instanceof Error ? a : Array(a + 1).join(b)
                },
                RIGHT: function(b, a) {
                    a = e.parseNumber(void 0 === a ? 1 : a);
                    return a instanceof Error ? a : b ? b.substring(b.length - a) : null
                },
                SEARCH: function(b, a, c) {
                    if ("string" !== typeof b || "string" !== typeof a) return h.value;
                    c = void 0 === c ? 0 : c;
                    b = a.toLowerCase().indexOf(b.toLowerCase(), c - 1) + 1;
                    return 0 === b ? h.value : b
                },
                SPLIT: function(b, a) {
                    return b.split(a)
                },
                SUBSTITUTE: function(b, a, c, d) {
                    if (b && a && c) {
                        if (void 0 === d) return b.replace(new RegExp(a, "g"),
                            c);
                        for (var f = 0, k = 0; 0 < b.indexOf(a, f);)
                            if (f = b.indexOf(a, f + 1), k++, k === d) return b.substring(0, f) + c + b.substring(f + a.length)
                    } else return b
                },
                T: function(b) {
                    return "string" === typeof b ? b : ""
                },
                TEXT: null,
                TRIM: function(b) {
                    return "string" !== typeof b ? h.value : b.replace(/ +/g, " ").trim()
                }
            };
            g.UNICHAR = g.CHAR;
            g.UNICODE = g.CODE;
            g.UPPER = function(b) {
                return "string" !== typeof b ? h.value : b.toUpperCase()
            };
            g.VALUE = null;
            return g
        }();
        t.stats = function() {
            var g = {
                AVEDEV: null,
                AVERAGE: function() {
                    for (var b = e.numbers(e.flatten(arguments)), a =
                            b.length, c = 0, d = 0, f = 0; f < a; f++) c += b[f], d += 1;
                    return c / d
                },
                AVERAGEA: function() {
                    for (var b = e.flatten(arguments), a = b.length, c = 0, d = 0, f = 0; f < a; f++) {
                        var k = b[f];
                        "number" === typeof k && (c += k);
                        !0 === k && c++;
                        null !== k && d++
                    }
                    return c / d
                },
                AVERAGEIF: function(b, a, c) {
                    c = c || b;
                    b = e.flatten(b);
                    c = e.parseNumberArray(e.flatten(c));
                    if (c instanceof Error) return c;
                    for (var d = 0, f = 0, k = 0; k < b.length; k++) eval(b[k] + a) && (f += c[k], d++);
                    return f / d
                },
                AVERAGEIFS: null,
                COUNT: function() {
                    return e.numbers(e.flatten(arguments)).length
                },
                COUNTA: function() {
                    var b =
                        e.flatten(arguments);
                    return b.length - g.COUNTBLANK(b)
                },
                COUNTIN: function(b, a) {
                    for (var c = 0, d = 0; d < b.length; d++) b[d] === a && c++;
                    return c
                },
                COUNTBLANK: function() {
                    for (var b = e.flatten(arguments), a = 0, c, d = 0; d < b.length; d++) c = b[d], null !== c && "" !== c || a++;
                    return a
                },
                COUNTIF: function() {
                    var b = e.argsToArray(arguments),
                        a = b.pop();
                    b = e.flatten(b);
                    /[<>=!]/.test(a) || (a = '=="' + a + '"');
                    for (var c = 0, d = 0; d < b.length; d++) "string" !== typeof b[d] ? eval(b[d] + a) && c++ : eval('"' + b[d] + '"' + a) && c++;
                    return c
                },
                COUNTIFS: function() {
                    for (var b = e.argsToArray(arguments),
                            a = Array(e.flatten(b[0]).length), c = 0; c < a.length; c++) a[c] = !0;
                    for (c = 0; c < b.length; c += 2) {
                        var d = e.flatten(b[c]),
                            f = b[c + 1];
                        /[<>=!]/.test(f) || (f = '=="' + f + '"');
                        for (var k = 0; k < d.length; k++) a[k] = "string" !== typeof d[k] ? a[k] && eval(d[k] + f) : a[k] && eval('"' + d[k] + '"' + f)
                    }
                    for (c = b = 0; c < a.length; c++) a[c] && b++;
                    return b
                },
                COUNTUNIQUE: function() {
                    return UNIQUE.apply(null, e.flatten(arguments)).length
                },
                FISHER: function(b) {
                    b = e.parseNumber(b);
                    return b instanceof Error ? b : Math.log((1 + b) / (1 - b)) / 2
                },
                FISHERINV: function(b) {
                    b = e.parseNumber(b);
                    if (b instanceof Error) return b;
                    b = Math.exp(2 * b);
                    return (b - 1) / (b + 1)
                },
                FREQUENCY: function(b, a) {
                    b = e.parseNumberArray(e.flatten(b));
                    a = e.parseNumberArray(e.flatten(a));
                    if (e.anyIsError(b, a)) return h.value;
                    for (var c = b.length, d = a.length, f = [], k = 0; k <= d; k++)
                        for (var l = f[k] = 0; l < c; l++) 0 === k ? b[l] <= a[0] && (f[0] += 1) : k < d ? b[l] > a[k - 1] && b[l] <= a[k] && (f[k] += 1) : k === d && b[l] > a[d - 1] && (f[d] += 1);
                    return f
                },
                LARGE: function(b, a) {
                    b = e.parseNumberArray(e.flatten(b));
                    a = e.parseNumber(a);
                    return e.anyIsError(b, a) ? b : b.sort(function(c, d) {
                        return d -
                            c
                    })[a - 1]
                },
                MAX: function() {
                    var b = e.numbers(e.flatten(arguments));
                    return 0 === b.length ? 0 : Math.max.apply(Math, b)
                },
                MAXA: function() {
                    var b = e.arrayValuesToNumbers(e.flatten(arguments));
                    return 0 === b.length ? 0 : Math.max.apply(Math, b)
                },
                MIN: function() {
                    var b = e.numbers(e.flatten(arguments));
                    return 0 === b.length ? 0 : Math.min.apply(Math, b)
                },
                MINA: function() {
                    var b = e.arrayValuesToNumbers(e.flatten(arguments));
                    return 0 === b.length ? 0 : Math.min.apply(Math, b)
                },
                MODE: {}
            };
            g.MODE.MULT = function() {
                var b = e.parseNumberArray(e.flatten(arguments));
                if (b instanceof Error) return b;
                for (var a = b.length, c = {}, d = [], f = 0, k, l = 0; l < a; l++) k = b[l], c[k] = c[k] ? c[k] + 1 : 1, c[k] > f && (f = c[k], d = []), c[k] === f && (d[d.length] = k);
                return d
            };
            g.MODE.SNGL = function() {
                var b = e.parseNumberArray(e.flatten(arguments));
                return b instanceof Error ? b : g.MODE.MULT(b).sort(function(a, c) {
                    return a - c
                })[0]
            };
            g.PERCENTILE = {};
            g.PERCENTILE.EXC = function(b, a) {
                b = e.parseNumberArray(e.flatten(b));
                a = e.parseNumber(a);
                if (e.anyIsError(b, a)) return h.value;
                b = b.sort(function(d, f) {
                    return d - f
                });
                var c = b.length;
                if (a <
                    1 / (c + 1) || a > 1 - 1 / (c + 1)) return h.num;
                a = a * (c + 1) - 1;
                c = Math.floor(a);
                return e.cleanFloat(a === c ? b[a] : b[c] + (a - c) * (b[c + 1] - b[c]))
            };
            g.PERCENTILE.INC = function(b, a) {
                b = e.parseNumberArray(e.flatten(b));
                a = e.parseNumber(a);
                if (e.anyIsError(b, a)) return h.value;
                b = b.sort(function(d, f) {
                    return d - f
                });
                a *= b.length - 1;
                var c = Math.floor(a);
                return e.cleanFloat(a === c ? b[a] : b[c] + (a - c) * (b[c + 1] - b[c]))
            };
            g.PERCENTRANK = {};
            g.PERCENTRANK.EXC = function(b, a, c) {
                c = void 0 === c ? 3 : c;
                b = e.parseNumberArray(e.flatten(b));
                a = e.parseNumber(a);
                c = e.parseNumber(c);
                if (e.anyIsError(b, a, c)) return h.value;
                b = b.sort(function(r, u) {
                    return r - u
                });
                var d = UNIQUE.apply(null, b),
                    f = b.length,
                    k = d.length;
                c = Math.pow(10, c);
                for (var l = 0, m = !1, n = 0; !m && n < k;) a === d[n] ? (l = (b.indexOf(d[n]) + 1) / (f + 1), m = !0) : a >= d[n] && (a < d[n + 1] || n === k - 1) && (l = (b.indexOf(d[n]) + 1 + (a - d[n]) / (d[n + 1] - d[n])) / (f + 1), m = !0), n++;
                return Math.floor(l * c) / c
            };
            g.PERCENTRANK.INC = function(b, a, c) {
                c = void 0 === c ? 3 : c;
                b = e.parseNumberArray(e.flatten(b));
                a = e.parseNumber(a);
                c = e.parseNumber(c);
                if (e.anyIsError(b, a, c)) return h.value;
                b = b.sort(function(r,
                    u) {
                    return r - u
                });
                var d = UNIQUE.apply(null, b),
                    f = b.length,
                    k = d.length;
                c = Math.pow(10, c);
                for (var l = 0, m = !1, n = 0; !m && n < k;) a === d[n] ? (l = b.indexOf(d[n]) / (f - 1), m = !0) : a >= d[n] && (a < d[n + 1] || n === k - 1) && (l = (b.indexOf(d[n]) + (a - d[n]) / (d[n + 1] - d[n])) / (f - 1), m = !0), n++;
                return Math.floor(l * c) / c
            };
            g.PERMUT = function(b, a) {
                b = e.parseNumber(b);
                a = e.parseNumber(a);
                return e.anyIsError(b, a) ? h.value : FACT(b) / FACT(b - a)
            };
            g.PERMUTATIONA = function(b, a) {
                b = e.parseNumber(b);
                a = e.parseNumber(a);
                return e.anyIsError(b, a) ? h.value : Math.pow(b, a)
            };
            g.PHI =
                function(b) {
                    b = e.parseNumber(b);
                    return b instanceof Error ? h.value : Math.exp(-.5 * b * b) / 2.5066282746310002
                };
            g.PROB = function(b, a, c, d) {
                if (void 0 === c) return 0;
                d = void 0 === d ? c : d;
                b = e.parseNumberArray(e.flatten(b));
                a = e.parseNumberArray(e.flatten(a));
                c = e.parseNumber(c);
                d = e.parseNumber(d);
                if (e.anyIsError(b, a, c, d)) return h.value;
                if (c === d) return 0 <= b.indexOf(c) ? a[b.indexOf(c)] : 0;
                for (var f = b.sort(function(n, r) {
                        return n - r
                    }), k = f.length, l = 0, m = 0; m < k; m++) f[m] >= c && f[m] <= d && (l += a[b.indexOf(f[m])]);
                return l
            };
            g.QUARTILE = {};
            g.QUARTILE.EXC = function(b, a) {
                b = e.parseNumberArray(e.flatten(b));
                a = e.parseNumber(a);
                if (e.anyIsError(b, a)) return h.value;
                switch (a) {
                    case 1:
                        return g.PERCENTILE.EXC(b, .25);
                    case 2:
                        return g.PERCENTILE.EXC(b, .5);
                    case 3:
                        return g.PERCENTILE.EXC(b, .75);
                    default:
                        return h.num
                }
            };
            g.QUARTILE.INC = function(b, a) {
                b = e.parseNumberArray(e.flatten(b));
                a = e.parseNumber(a);
                if (e.anyIsError(b, a)) return h.value;
                switch (a) {
                    case 1:
                        return g.PERCENTILE.INC(b, .25);
                    case 2:
                        return g.PERCENTILE.INC(b, .5);
                    case 3:
                        return g.PERCENTILE.INC(b,
                            .75);
                    default:
                        return h.num
                }
            };
            g.RANK = {};
            g.RANK.AVG = function(b, a, c) {
                b = e.parseNumber(b);
                a = e.parseNumberArray(e.flatten(a));
                if (e.anyIsError(b, a)) return h.value;
                a = e.flatten(a);
                a = a.sort(c ? function(k, l) {
                    return k - l
                } : function(k, l) {
                    return l - k
                });
                c = a.length;
                for (var d = 0, f = 0; f < c; f++) a[f] === b && d++;
                return 1 < d ? (2 * a.indexOf(b) + d + 1) / 2 : a.indexOf(b) + 1
            };
            g.RANK.EQ = function(b, a, c) {
                b = e.parseNumber(b);
                a = e.parseNumberArray(e.flatten(a));
                if (e.anyIsError(b, a)) return h.value;
                a = a.sort(c ? function(d, f) {
                    return d - f
                } : function(d, f) {
                    return f -
                        d
                });
                return a.indexOf(b) + 1
            };
            g.RSQ = function(b, a) {
                b = e.parseNumberArray(e.flatten(b));
                a = e.parseNumberArray(e.flatten(a));
                return e.anyIsError(b, a) ? h.value : Math.pow(g.PEARSON(b, a), 2)
            };
            g.SMALL = function(b, a) {
                b = e.parseNumberArray(e.flatten(b));
                a = e.parseNumber(a);
                return e.anyIsError(b, a) ? b : b.sort(function(c, d) {
                    return c - d
                })[a - 1]
            };
            g.STANDARDIZE = function(b, a, c) {
                b = e.parseNumber(b);
                a = e.parseNumber(a);
                c = e.parseNumber(c);
                return e.anyIsError(b, a, c) ? h.value : (b - a) / c
            };
            g.STDEV = {};
            g.STDEV.P = function() {
                var b = g.VAR.P.apply(this,
                    arguments);
                return Math.sqrt(b)
            };
            g.STDEV.S = function() {
                var b = g.VAR.S.apply(this, arguments);
                return Math.sqrt(b)
            };
            g.STDEVA = function() {
                var b = g.VARA.apply(this, arguments);
                return Math.sqrt(b)
            };
            g.STDEVPA = function() {
                var b = g.VARPA.apply(this, arguments);
                return Math.sqrt(b)
            };
            g.VAR = {};
            g.VAR.P = function() {
                for (var b = e.numbers(e.flatten(arguments)), a = b.length, c = 0, d = g.AVERAGE(b), f = 0; f < a; f++) c += Math.pow(b[f] - d, 2);
                return c / a
            };
            g.VAR.S = function() {
                for (var b = e.numbers(e.flatten(arguments)), a = b.length, c = 0, d = g.AVERAGE(b),
                        f = 0; f < a; f++) c += Math.pow(b[f] - d, 2);
                return c / (a - 1)
            };
            g.VARA = function() {
                for (var b = e.flatten(arguments), a = b.length, c = 0, d = 0, f = g.AVERAGEA(b), k = 0; k < a; k++) {
                    var l = b[k];
                    c = "number" === typeof l ? c + Math.pow(l - f, 2) : !0 === l ? c + Math.pow(1 - f, 2) : c + Math.pow(0 - f, 2);
                    null !== l && d++
                }
                return c / (d - 1)
            };
            g.VARPA = function() {
                for (var b = e.flatten(arguments), a = b.length, c = 0, d = 0, f = g.AVERAGEA(b), k = 0; k < a; k++) {
                    var l = b[k];
                    c = "number" === typeof l ? c + Math.pow(l - f, 2) : !0 === l ? c + Math.pow(1 - f, 2) : c + Math.pow(0 - f, 2);
                    null !== l && d++
                }
                return c / d
            };
            g.WEIBULL = {};
            g.WEIBULL.DIST = function(b, a, c, d) {
                b = e.parseNumber(b);
                a = e.parseNumber(a);
                c = e.parseNumber(c);
                return e.anyIsError(b, a, c) ? h.value : d ? 1 - Math.exp(-Math.pow(b / c, a)) : Math.pow(b, a - 1) * Math.exp(-Math.pow(b / c, a)) * a / Math.pow(c, a)
            };
            g.Z = {};
            g.Z.TEST = function(b, a, c) {
                b = e.parseNumberArray(e.flatten(b));
                a = e.parseNumber(a);
                if (e.anyIsError(b, a)) return h.value;
                c = c || g.STDEV.S(b);
                var d = b.length;
                return 1 - g.NORM.S.DIST((g.AVERAGE(b) - a) / (c / Math.sqrt(d)), !0)
            };
            return g
        }();
        t.utils = function() {
            return {
                PROGRESS: function(g, b) {
                    return '<div style="width:' +
                        (g ? g : "0") + "%;height:4px;background-color:" + (b ? b : "red") + ';margin-top:1px;"></div>'
                },
                RATING: function(g) {
                    for (var b = '<div class="jrating">', a = 0; 5 > a; a++) b = a < g ? b + '<div class="jrating-selected"></div>' : b + "<div></div>";
                    return b + "</div>"
                }
            }
        }();
        for (var z = 0; z < Object.keys(t).length; z++)
            for (var C = t[Object.keys(t)[z]], B = Object.keys(C), A = 0; A < B.length; A++)
                if (C[B[A]])
                    if ("function" == typeof C[B[A]] || "object" == typeof C[B[A]]) {
                        if (q[B[A]] = C[B[A]], q[B[A]].toString = function() {
                                return "#ERROR"
                            }, "object" == typeof C[B[A]])
                            for (var J =
                                    Object.keys(C[B[A]]), H = 0; H < J.length; H++) q[B[A]][J[H]].toString = function() {
                                return "#ERROR"
                            }
                    } else q[B[A]] = function() {
                        return B[A] + "Not implemented"
                    };
        else q[B[A]] = function() {
            return B[A] + "Not implemented"
        };
        var I = null,
            F = null,
            G = null;
        q.TABLE = function() {
            return G
        };
        q.COLUMN = q.COL = function() {
            return parseInt(I) + 1
        };
        q.ROW = function() {
            return parseInt(F) + 1
        };
        q.CELL = function() {
            return D.getColumnNameFromCoords(I, F)
        };
        q.VALUE = function(g, b, a) {
            return G.getValueFromCoords(parseInt(g) - 1, parseInt(b) - 1, a)
        };
        q.THISROWCELL = function(g) {
            return G.getValueFromCoords(parseInt(g) -
                1, parseInt(F))
        };
        var K = function(g, b) {
                for (var a = 0; a < g.length; a++) {
                    var c = D.getTokensFromRange(g[a]);
                    b = b.replace(g[a], "[" + c.join(",") + "]")
                }
                return b
            },
            D = function(g, b, a, c, d) {
                G = d;
                I = a;
                F = c;
                c = "";
                d = Object.keys(b);
                if (d.length) {
                    var f = {};
                    for (a = 0; a < d.length; a++)
                        if (k = d[a].replace(/!/g, "."), 0 < k.indexOf(".")) {
                            var k = k.split(".");
                            f[k[0]] = {}
                        } k = Object.keys(f);
                    for (a = 0; a < k.length; a++) c += "var " + k[a] + " = {};";
                    for (a = 0; a < d.length; a++) k = d[a].replace(/!/g, "."), f = b[d[a]], (isNaN(f) || null === f || "" === f) && (f = b[d[a]].match(/(('.*?'!)|(\w*!))?(\$?[A-Z]+\$?[0-9]*):(\$?[A-Z]+\$?[0-9]*)?/g)) &&
                        f.length && (b[d[a]] = K(f, b[d[a]])), c = 0 < k.indexOf(".") ? c + (k + " = " + b[d[a]] + ";\n") : c + ("var " + k + " = " + b[d[a]] + ";\n")
                }
                g = g.replace(/\$/g, "");
                g = g.replace(/!/g, ".");
                b = "";
                a = 0;
                d = ["=", "!", ">", "<"];
                for (k = 0; k < g.length; k++) '"' == g[k] && (a = 0 == a ? 1 : 0), 1 == a ? b += g[k] : (b += g[k].toUpperCase(), 0 < k && "=" == g[k] && -1 == d.indexOf(g[k - 1]) && -1 == d.indexOf(g[k + 1]) && (b += "="));
                b = b.replace(/\^/g, "**");
                b = b.replace(/<>/g, "!=");
                b = b.replace(/&/g, "+");
                g = b = b.replace(/\$/g, "");
                (f = g.match(/(('.*?'!)|(\w*!))?(\$?[A-Z]+\$?[0-9]*):(\$?[A-Z]+\$?[0-9]*)?/g)) &&
                f.length && (g = K(f, g));
                return (new Function(c + "; return " + g))()
            };
        D.getColumnNameFromCoords = function(g, b) {
            g = parseInt(g);
            var a = "";
            701 < g ? (a += String.fromCharCode(64 + parseInt(g / 676)), a += String.fromCharCode(64 + parseInt(g % 676 / 26))) : 25 < g && (a += String.fromCharCode(64 + parseInt(g / 26)));
            a += String.fromCharCode(65 + g % 26);
            return a + (parseInt(b) + 1)
        };
        D.getCoordsFromColumnName = function(g) {
            var b = /^[a-zA-Z]+/.exec(g);
            if (b) {
                for (var a = 0, c = 0; c < b[0].length; c++) a += parseInt(b[0].charCodeAt(c) - 64) * Math.pow(26, b[0].length - 1 - c);
                a--;
                0 > a && (a = 0);
                g = parseInt(/[0-9]+$/.exec(g)) || null;
                0 < g && g--;
                return [a, g]
            }
        };
        D.getRangeFromTokens = function(g) {
            g = g.filter(function(d) {
                return "#REF!" != d
            });
            for (var b = "", a = "", c = 0; c < g.length; c++) 0 <= g[c].indexOf(".") ? b = "." : 0 <= g[c].indexOf("!") && (b = "!"), b && (a = g[c].split(b), g[c] = a[1], a = a[0] + b);
            g.sort(function(d, f) {
                d = Helpers.getCoordsFromColumnName(d);
                f = Helpers.getCoordsFromColumnName(f);
                return d[1] > f[1] ? 1 : d[1] < f[1] ? -1 : d[0] > f[0] ? 1 : d[0] < f[0] ? -1 : 0
            });
            return g.length ? a + (g[0] + ":" + g[g.length - 1]) : "#REF!"
        };
        D.getTokensFromRange =
            function(g) {
                if (0 < g.indexOf(".")) {
                    var b = g.split(".");
                    g = b[1];
                    b = b[0] + "."
                } else 0 < g.indexOf("!") ? (b = g.split("!"), g = b[1], b = b[0] + "!") : b = "";
                g = g.split(":");
                var a = D.getCoordsFromColumnName(g[0]),
                    c = D.getCoordsFromColumnName(g[1]);
                if (a[0] <= c[0]) {
                    g = a[0];
                    var d = c[0]
                } else g = c[0], d = a[0];
                if (null === a[1] && null == c[1])
                    for (var f = null, k = null, l = Object.keys(vars), m = 0; m < l.length; m++) {
                        var n = D.getCoordsFromColumnName(l[m]);
                        n[0] === a[0] && (null === f || n[1] < f) && (f = n[1]);
                        n[0] === c[0] && (null === k || n[1] > k) && (k = n[1])
                    } else a[1] <= c[1] ? (f =
                        a[1], k = c[1]) : (f = c[1], k = a[1]);
                for (a = []; f <= k; f++) {
                    c = [];
                    for (m = g; m <= d; m++) c.push(b + D.getColumnNameFromCoords(m, f));
                    a.push(c)
                }
                return a
            };
        D.setFormula = function(g) {
            for (var b = Object.keys(g), a = 0; a < b.length; a++) "function" == typeof g[b[a]] && (q[b[a]] = g[b[a]])
        };
        return D
    };
    return "undefined" !== typeof window ? p(window) : null
});

if (!jSuites && typeof(require) === 'function') {
    var jSuites = require('jsuites');
}

var $jscomp = $jscomp || {};
$jscomp.scope = {};
$jscomp.arrayIteratorImpl = function(z) {
    var D = 0;
    return function() {
        return D < z.length ? {
            done: !1,
            value: z[D++]
        } : {
            done: !0
        }
    }
};
$jscomp.arrayIterator = function(z) {
    return {
        next: $jscomp.arrayIteratorImpl(z)
    }
};
$jscomp.ASSUME_ES5 = !1;
$jscomp.ASSUME_NO_NATIVE_MAP = !1;
$jscomp.ASSUME_NO_NATIVE_SET = !1;
$jscomp.SIMPLE_FROUND_POLYFILL = !1;
$jscomp.ISOLATE_POLYFILLS = !1;
$jscomp.FORCE_POLYFILL_PROMISE = !1;
$jscomp.FORCE_POLYFILL_PROMISE_WHEN_NO_UNHANDLED_REJECTION = !1;
$jscomp.defineProperty = $jscomp.ASSUME_ES5 || "function" == typeof Object.defineProperties ? Object.defineProperty : function(z, D, F) {
    if (z == Array.prototype || z == Object.prototype) return z;
    z[D] = F.value;
    return z
};
$jscomp.getGlobal = function(z) {
    z = ["object" == typeof globalThis && globalThis, z, "object" == typeof window && window, "object" == typeof self && self, "object" == typeof global && global];
    for (var D = 0; D < z.length; ++D) {
        var F = z[D];
        if (F && F.Math == Math) return F
    }
    throw Error("Cannot find global object");
};
$jscomp.global = $jscomp.getGlobal(this);
$jscomp.IS_SYMBOL_NATIVE = "function" === typeof Symbol && "symbol" === typeof Symbol("x");
$jscomp.TRUST_ES6_POLYFILLS = !$jscomp.ISOLATE_POLYFILLS || $jscomp.IS_SYMBOL_NATIVE;
$jscomp.polyfills = {};
$jscomp.propertyToPolyfillSymbol = {};
$jscomp.POLYFILL_PREFIX = "$jscp$";
var $jscomp$lookupPolyfilledValue = function(z, D) {
    var F = $jscomp.propertyToPolyfillSymbol[D];
    if (null == F) return z[D];
    F = z[F];
    return void 0 !== F ? F : z[D]
};
$jscomp.polyfill = function(z, D, F, O) {
    D && ($jscomp.ISOLATE_POLYFILLS ? $jscomp.polyfillIsolated(z, D, F, O) : $jscomp.polyfillUnisolated(z, D, F, O))
};
$jscomp.polyfillUnisolated = function(z, D, F, O) {
    F = $jscomp.global;
    z = z.split(".");
    for (O = 0; O < z.length - 1; O++) {
        var M = z[O];
        if (!(M in F)) return;
        F = F[M]
    }
    z = z[z.length - 1];
    O = F[z];
    D = D(O);
    D != O && null != D && $jscomp.defineProperty(F, z, {
        configurable: !0,
        writable: !0,
        value: D
    })
};
$jscomp.polyfillIsolated = function(z, D, F, O) {
    var M = z.split(".");
    z = 1 === M.length;
    O = M[0];
    O = !z && O in $jscomp.polyfills ? $jscomp.polyfills : $jscomp.global;
    for (var V = 0; V < M.length - 1; V++) {
        var K = M[V];
        if (!(K in O)) return;
        O = O[K]
    }
    M = M[M.length - 1];
    F = $jscomp.IS_SYMBOL_NATIVE && "es6" === F ? O[M] : null;
    D = D(F);
    null != D && (z ? $jscomp.defineProperty($jscomp.polyfills, M, {
        configurable: !0,
        writable: !0,
        value: D
    }) : D !== F && (void 0 === $jscomp.propertyToPolyfillSymbol[M] && (F = 1E9 * Math.random() >>> 0, $jscomp.propertyToPolyfillSymbol[M] = $jscomp.IS_SYMBOL_NATIVE ?
        $jscomp.global.Symbol(M) : $jscomp.POLYFILL_PREFIX + F + "$" + M), $jscomp.defineProperty(O, $jscomp.propertyToPolyfillSymbol[M], {
        configurable: !0,
        writable: !0,
        value: D
    })))
};
$jscomp.initSymbol = function() {};
$jscomp.iteratorPrototype = function(z) {
    z = {
        next: z
    };
    z[Symbol.iterator] = function() {
        return this
    };
    return z
};
$jscomp.iteratorFromArray = function(z, D) {
    z instanceof String && (z += "");
    var F = 0,
        O = !1,
        M = {
            next: function() {
                if (!O && F < z.length) {
                    var V = F++;
                    return {
                        value: D(V, z[V]),
                        done: !1
                    }
                }
                O = !0;
                return {
                    done: !0,
                    value: void 0
                }
            }
        };
    M[Symbol.iterator] = function() {
        return M
    };
    return M
};
$jscomp.polyfill("Array.prototype.values", function(z) {
    return z ? z : function() {
        return $jscomp.iteratorFromArray(this, function(D, F) {
            return F
        })
    }
}, "es8", "es3");
var _$_43fc = ["use strict", "{", "}", "", "8.1.1", "Base", "https://jspreadsheet.com", "Unlicensed", "Jspreadsheet Pro\r\n", " edition ", "\r\n", "set", "=", "undefined", "number", "fullscreen", "block", "string", "readonly", "jss_hidden_index", "div", "right", "10px", "pointer", "/v8", ",", "localhost", ".", "v8", " (Expired)", "Licensed to: ", "Premium", "License required", "A valid license is required", "This version is not included on the scope of this license", "Your license is expired", "jspreadsheet", "img", "/jspreadsheet/", "logo.png",
    "none", "status", "data-x", "data-y", "px", "X", "goto", "jss_scroll", "jss_control", "hidden", "jss_freezed", "get", "colspan", "center", "300px", "Jspreadsheet Pro", "text", "autocomplete", "dropdown", "GET", "json", "Jspreadsheet Alert: secureFormulas is set to false.", "setConfig", "insertRow", "deleteRow", "insertColumn", "deleteColumn", "moveRow", "moveColumn", "setMerge", "removeMerge", "setStyle", "resetStyle", "setWidth", "setHeight", "setHeader", "setComments", "setProperty", "orderBy", "setValue", "renameWorksheet", "moveWorksheet",
    "onundo", "onredo", "object", "function", "contextMenu", "toolbar", "Jspreadsheet: cursor not in the viewport", "oneditionstart", "oncreateeditor", "jss_focus", "jss_formula", "oneditionend", "data-mask", "data-value", "inputmode", "0", "style", "jss_input", "contentEditable", "onevent", "setRowId", "onchangerowid", "setBorder", "resetBorders", "updateCells", "setFormula", "updateData", "onbeforesave", "POST", "Authorization", "Bearer ", "disconected", ": ", "Sorry, something went wrong, refreshing your spreadsheet...", "_top", "onsave",
    "persistence", "?", "?worksheet=", "&worksheet=", "add", "remove", "jss_cursor", "selected", "main", "copying", "onblur", "onfocus", "selection", "onselection", " ", "'", "'!", "!", ":", "jss_border", "cloning", "jss_border_", "15", "-2000px", "#ccc", "transparent", "0px", "tr", "jss_group_container", "td", "&nbsp;", ".jss_group", "jss_group", "+", "width", "Jspreadsheet: This column is part of a merged cell.", "jss_dragging", "1px", "jss_helper_col", "Jspreadsheet: This row is part of a merged cell", "Jspreadsheet: Please clear your search before perform this action",
    "51px", "jss_helper_row", "No cell selected", "Invalid merged properties", "onmerge", "rowspan", "Column name", "data-title", "onchangeheader", "jss_filters_icon", "col", "setFooter", "onchangefooter", "setFooterValue", "onchangefootervalue", "resetFooter", "setNestedHeaders", "onchangenested", "jss_nested", "resetNestedHeaders", "setNestedCell", "onchangenestedcell", "data-nested-x", "data-nested-y", "align", "calendar", "-", "00:00:00", '"', "g", '""', "\t", "oncopy", "copy", "color: initial", "JSS: For better results, please allow pagination or tableOverflow.",
    "onbeforepaste", "onpaste", "Export not allowed", "\ufeff", "text/csv;charset=utf-8;", ".csv", "a", "download", "setDefinedNames", "#1a237e", "#b71c1c", "#880e4f", "#1b5e20", "#ff6f00", "/", "*", "^", "(", "[", "|", "<", ">", "data-token", "jss_picker", "jss_object", "input", "contenteditable", "active", "last", '<b class="', '" data-token="', '">', "</b>", "data-single", "true", "b", "&", ")", "]", "var ", " = {};", " = ", ";\n", "; return ", "#LOOP", "Reference loop detected", "#SELF", "Self Reference detected", "#ERROR", "Table does not exist or is not loaded yet",
    "#SPILL!", "#REF!", "$", "Something went wrong", "onafterchanges", "checkbox", "autonumber", "radio", "email", "url", "color", "progressbar", "rating", "onbeforechange", "onchange", "oncreatecell", "values", "mergeCells", ";", "title", "jss_unlocked", "oncreaterow", "jss_row", "Jspreadsheet: Invalid origin or destination", "onmoverow", "Jspreadsheet: Insert row is not enabled in the table configuration", "onbeforeinsertrow", "onbeforeinsertrow returned false", "oninsertrow", "Jspreadsheet: Delete row is not enabled in the table configuration",
    "onbeforedeleterow", "onbeforedeleterow returned false", "meta", "cells", "comments", "ondeleterow", "Jspreadsheet: Invalid origin", "Jspreadsheet: Invalid destination", "onmovecolumn", "Jspreadsheet: Insert column is not enabled in the table configuration", "onbeforeinsertcolumn", "onbeforeinsertcolumn returned false", "col_", "oninsertcolumn", "Jspreadsheet: Delete column is not enabled in the table configuration", "Jspreadsheet: it is not possible to delete the last column", "onbeforedeletecolumn", "onbeforedeletecolumn returned false",
    "ondeletecolumn", "Jspreadsheet: Column does not exists.", "; ", "onchangestyle", "No cell provided", "onresetstyle", "onbeforecomments", "oncomments", "setMeta", "onchangemeta", "resetMeta", "onresetmeta", "arrow-down", "percent", "numeric", "onbeforesort", "This action will destroy any existing merged cells. Are you sure?", "onsort", "arrow-up", "jss_pagination", "Jspreadsheet: No pagination defined", "Jspreadsheet: pagination not defined", "onbeforechangepage", "onchangepage", "No records found", "jss_page", "jss_page_selected",
    "Showing page {0} of {1} entries", "jss_content", "wheel", "selectstart", "table", "thead", "tbody", "tfoot", "colgroup", "50", "jss_selectall", "jss", "cellpadding", "cellspacing", "unselectable", "yes", "jss_wrap", "jss_overflow", "jss_locked", "jss_corner", "on", "onselectstart", "return false", "Y", "jss_table", "jss_table_container", "both", "Freeze columns cannot be greater than the number of available columns", "rows", "jss_container", "jss_toolbar", "jss_helper", "jss_loading", "textarea", "jss_textarea", "-1", "bottom", "Defined names are only available on the Premium edition.",
    "beforeinit", "Sheet", "Worksheet clash name: ", ". It is highly recommended to define a unique worksheetName on the initialization.", "jss_worksheet", "init", "To improve the performance, use tableOverflow or pagination.", "resize", "touchstart", "touchmove", "touchend", "onresizecolumn", "onresizerow", "label", "jss_filters_active", "Jspreadsheet: the filter is not enabled.", "Blanks", "onbeforefilter", "onfilter", "gi", "jss_filters_options", "INPUT", "Search", "jss_filters_search", "button", "Ok", "jss_filters_apply",
    "jss_filters", '<input type="checkbox"> ', "Add current selection to filter", '<input type="checkbox"> (', "Select all", "select", "jss_pagination_dropdown", "option", "Show", "entries", "jss_search", "onbeforesearch", "onsearch", "jss_search_container", "jss_contextmenu", "Mac", "&#8984;", "Ctrl", "tabs", "Rename this worksheet", "Delete this worksheet", "Are you sure?", "line", "nested", "Rename this cell", "header", "row", "cell", "Cut", "content_cut", " + X", "Copy", "content_copy", " + C", "Paste", "content_paste", " + V", "Insert a new column before",
    "Insert a new column after", "Delete selected columns", "Rename this column", "Create a new row", "Order ascending", "Order descending", "Insert a new row before", "Insert a new row after", "Delete selected rows", "Edit comments", "Add comments", "notes", "Comments", "Clear comments", "Save as", "save", " + S", "About", "info", "with-toolbar", "i", "undo", "redo", "divisor", "160px", "Verdana", "Arial", "Courier New", '<span style="font-family:', "</span>", "font-family", "48px", "format_size", "x-small", "small", "medium", "large",
    "x-large", '<span style="font-size:', "font-size", "format_align_left", "format_align_center", "format_align_right", "format_align_justify", '<i class="material-icons">', "</i>", "text-align", "_", "format_bold", "font-weight", "bold", "format_color_text", "format_color_fill", "background-color", "height", "50px", "Normal", "Medium", "Large", "Extra", "jss_row_medium", "jss_row_large", "jss_row_extra", "fullscreen_exit", "Toggle Fullscreen", "border_all", "border_outer", "border_inner", "border_horizontal", "border_vertical", "border_left",
    "border_top", "border_right", "border_bottom", "border_clear", "black", "border-left: ", "px solid ", "border-left: ; ", "border-right: ", "border-right: ; ", "border-top: ", "border-top: ; ", "border-bottom: ", "border-bottom: ; ", "material-icons", "color_lens", '<div style="height: ', 'px; width: 50px; background-color: black;"></div>', "80px", "1", "search", "search_off", "Toggle Search", "onbeforecreateworksheet", "There is one existing worksheet with the same name.", "createWorksheet", "oncreateworksheet", "deleteWorksheet",
    "ondeleteworksheet", "It was not possible to rename worksheet due conflict name", "onrenameworksheet", "onmoveworksheet", "onopenworksheet", "jss_", "TABLE", "Element is not a table", "colgroup > col", "data-celltype", "readOnly", "name", "id", ":scope > thead > tr", ":scope > tr, :scope > tbody > tr", "data-formula", "class", "styleBold", "; font-weight:bold;", "font-weight:bold;", "tfoot tr", "\r", "\n", "jss_ignore", "THEAD", "TBODY", "TFOOT", "selectall", "header-group", "jtabs-headers", "filters", "footer", "jtoolbar", "DIV",
    "data-current", "html", "jss_nowrap", "insertHTML", "<br/>\n", "Process", "A", "mailto:", "_blank", "onclick", "jclose", "col-resize", "row-resize", "move", "Cloud plugin not loaded.", "jtabs", "jss_dialog", "#", "decimal", "data-locale", "%", "jss_percent", "\\[Red\\]", "red", "\\(", "jss_notes", "145px", "tag", "jss_dropdown_tags", "jss_dropdown", "multiple", "image", "<div class='jss_dropdown_icon' style='background-color: ", "'></div>", "orange", "<div class='jss_dropdown_tag' style='background-color: ", "'>", "</div>", "&lt;", "timepicker",
    "YYYY-MM-DD", "square", "100px", "false", "FALSE", "disabled", "TRUE", "jss_progressbar", "min", "max", "jss_rating", " stars", "star", "href", "IMG", "round", "100%", "absolute", "jfile", '<img src="', '" alt="">', "jss_richtext", "jss_tags", "40px", "#NOTFOUND", "<img src='", "' class='round small' />", "Jspreadsheet: worksheet not found "
];
(function(z, D) {
    typeof exports === _$_43fc[84] && typeof module !== _$_43fc[13] ? module.exports = D() : typeof define === _$_43fc[85] && define.amd ? define(D) : z.jspreadsheet = z.jexcel = D()
})(this, function() {
    _$_43fc[0];
    var z = function(k, a) {
            k = jSuites.translate(k);
            if (a && a.length)
                for (var c = 0; c < a.length; c++) k = k.replace(_$_43fc[1] + c + _$_43fc[2], a[c]);
            return k
        },
        D = function(k, a) {
            Array.isArray(k) && (k = k[0]);
            k = k.replace(new RegExp(/'/g), _$_43fc[3]).toUpperCase();
            if (void 0 === a) return window[k] ? window[k] : null;
            window[k] = a
        },
        F = function() {
            var k = {
                version: _$_43fc[4],
                edition: _$_43fc[5],
                host: _$_43fc[6],
                license: _$_43fc[7],
                print: function() {
                    return [_$_43fc[8] + this.edition + _$_43fc[9] + this.version + _$_43fc[10] + this.host + _$_43fc[10] + this.license]
                }
            };
            return function() {
                return k
            }
        }(),
        O = function(k) {
            var a = parseInt(k.y);
            if (k.id) var c = parseInt(k.id);
            else k.value && (c = parseInt(k.value));
            0 < c && (this.rows[a].id = c, pa[_$_43fc[11]].call(this, c))
        },
        M = function(k) {
            return (_$_43fc[3] + k).substr(0, 1) === _$_43fc[12]
        },
        V = function(k) {
            var a = this.options.columns;
            return a[k] && a[k].name ?
                a[k].name : k
        },
        K = function(k, a, c) {
            var b;
            if (b = this.options.data[a]) {
                var d = 0 <= k ? V.call(this, k) : k;
                typeof c === _$_43fc[13] ? c = typeof d === _$_43fc[14] ? b[k] : jSuites.path.call(b, _$_43fc[3] + d) : (typeof d === _$_43fc[14] ? b[k] = c : jSuites.path.call(b, _$_43fc[3] + d, c), !M(c) && this.records[a][k] && (this.records[a][k].v = c))
            } else c = null;
            return c
        },
        N = function(k) {
            return 0 == this.config.editable || k && 0 == k.options.editable ? !1 : (k = Fa.call(this)) ? 7 <= k ? !0 : !1 : !0
        },
        Ga = function(k) {
            var a = _$_43fc[15];
            if (this.parent) var c = this.parent.config,
                b = this.parent.element,
                d = this;
            else c = this.config, b = this.element, d = t.current ? t.current : null;
            typeof k === _$_43fc[13] && (k = !b.classList.contains(a));
            k ? (b.classList.add(a), c.fullscreen = !0) : (b.classList.remove(a), c.fullscreen = !1);
            d && (d.tbody.innerHTML = _$_43fc[3], C.call(d), P.refresh.call(d), ca.update.call(d.parent, d))
        },
        Ha = function(k) {
            var a = this.loading;
            null === k && (k = a.style.display == _$_43fc[16] ? !0 : !1);
            a.style.display = k ? _$_43fc[16] : _$_43fc[3];
            setTimeout(function() {
                a.style.display = _$_43fc[3]
            }, 1E3)
        },
        Ca = function(k,
            a) {
            if (typeof k === _$_43fc[17]) {
                var c = A.getCoordsFromColumnName(k);
                c = this.records[c[1]][c[0]];
                if (c.element) k = c.element;
                else return typeof a === _$_43fc[13] ? c.readonly : c.readonly = a ? !0 : !1
            }
            c = _$_43fc[18];
            k = k.classList;
            if (typeof a === _$_43fc[13]) return k.contains(c) ? !0 : !1;
            a ? k.add(c) : k.remove(c)
        },
        va = function(k) {
            var a = _$_43fc[19],
                c = this.table.classList;
            if (typeof k == _$_43fc[13]) return c.contains(a) ? !1 : !0;
            k ? c.remove(a) : c.add(a);
            0 < this.options.freezeColumns && ia[_$_43fc[11]].call(this, this.options.freezeColumns);
            this.refreshBorders()
        },
        Fa = function(k) {
            if (void 0 === k) return this.status;
            var a = document.createElement(_$_43fc[20]);
            a.style.textAlign = _$_43fc[21];
            a.style.fontSize = _$_43fc[22];
            a.style.cursor = _$_43fc[23];
            a.onclick = function() {
                window.location.href = F().host + _$_43fc[24]
            };
            var c = null,
                b = [74, 50, 48, 50, 48, 33].join(_$_43fc[3]);
            (function(e) {
                c = 1;
                try {
                    if (e) {
                        var f = window.atob(e);
                        f = f.split(_$_43fc[25]);
                        if (f[1]) {
                            f[1] = window.atob(f[1]);
                            var g = f[0];
                            e = b;
                            var h = f[1],
                                l = jSuites.sha512,
                                n = _$_43fc[3],
                                q = _$_43fc[3];
                            128 < e.length && (e =
                                l(e));
                            for (var p = 0; 128 > p; p++) {
                                var r = e[p] ? e[p].charCodeAt(0) : 0;
                                n += String.fromCharCode(54 ^ r);
                                q += String.fromCharCode(92 ^ r)
                            }
                            var w = l(q + l(n + h));
                            if (g != w) c = 3;
                            else if (f[1] = JSON.parse(f[1]), f[1].date) {
                                var m = window.location.hostname || _$_43fc[26],
                                    v = m.split(_$_43fc[27]);
                                m = m.split(_$_43fc[27]);
                                2 < m.length && !jSuites.isNumeric(m[m.length - 1]) && v.shift();
                                v = v.join(_$_43fc[27]);
                                m = m.join(_$_43fc[27]);
                                if (-1 == f[1].domain.indexOf(v) && -1 == f[1].domain.indexOf(m)) c = 4;
                                else if (f[1].scope && 0 <= f[1].scope.indexOf(_$_43fc[28])) {
                                    m =
                                        new Date;
                                    v = parseInt(m.getTime() / 1E3);
                                    var u = F();
                                    f[1].date < v ? (c = f[1].date + 2592E3 < v && !f[1].plan || f[1].date + 15552E3 < v ? 6 : 7, u.license = f[1].name + _$_43fc[29]) : (c = 8, u.license = _$_43fc[30] + f[1].name, u.edition = 3 == f[1].plan || 6 == f[1].plan ? _$_43fc[31] : _$_43fc[5])
                                } else c = 5
                            } else c = 4
                        } else c = 2
                    }
                } catch (x) {}
            })(k);
            k = _$_43fc[3];
            if (1 == c) k = _$_43fc[32];
            else if (2 == c || 3 == c || 4 == c) k = _$_43fc[33];
            else if (5 == c) k = _$_43fc[34];
            else if (6 == c || 7 == c) k = _$_43fc[35];
            a.appendChild(document.createTextNode(k));
            if (8 > c) try {
                if (typeof sessionStorage !==
                    _$_43fc[13] && !sessionStorage.getItem(_$_43fc[36])) {
                    sessionStorage.setItem(_$_43fc[36], !0);
                    var d = document.createElement(_$_43fc[37]);
                    d.src = F().host + _$_43fc[38] + _$_43fc[39];
                    d.style.display = _$_43fc[40];
                    a.appendChild(d)
                }
            } catch (e) {}
            Object.defineProperty(this, _$_43fc[41], {
                value: c,
                writable: !1,
                configurable: !1,
                enumerable: !1,
                extensible: !1
            });
            F().edition === _$_43fc[31] && (this.edition = 1);
            return a
        },
        P = function() {
            var k = function() {
                    if (this.edition) {
                        var d = this.edition.getAttribute(_$_43fc[42]),
                            e = this.edition.getAttribute(_$_43fc[43]);
                        Z.position.call(this, d, e)
                    }
                },
                a = function() {
                    this.scrollX.scrollWidth < this.scrollX.scrollLeft + this.scrollX.offsetWidth + 25 ? (this.content.scrollLeft = this.content.scrollWidth, 0 < this.options.freezeColumns && da.refresh.call(this)) : this.content.scrollLeft = 0;
                    k.call(this)
                },
                c = function() {
                    var d = this.scrollY.scrollTop + this.scrollY.offsetHeight + 13,
                        e = this.tbody.lastChild.getAttribute(_$_43fc[43]),
                        f = this.results ? this.rows[this.results[this.results.length - 1]].y : this.rows[this.rows.length - 1].y;
                    this.content.scrollTop = this.scrollY.scrollHeight <
                        d && e == f ? this.content.scrollHeight : 0;
                    k.call(this)
                },
                b = function() {
                    if (C.limited.call(this)) {
                        var d = ia.getWidth.call(this),
                            e = this.content.offsetWidth,
                            f = this.content.offsetHeight;
                        this.width = b.width.call(this) + (d ? d : 1);
                        this.height = b.height.call(this);
                        this.tfoot.offsetHeight && (this.height += this.tfoot.offsetHeight + 26);
                        this.scrollX.firstChild.style.width = this.width + _$_43fc[44];
                        this.scrollY.firstChild.style.height = this.height + _$_43fc[44];
                        this.scrollX.style.width = e + _$_43fc[44];
                        this.scrollY.style.height = f + _$_43fc[44];
                        this.scrollX.style.display = this.width > e ? _$_43fc[3] : _$_43fc[40];
                        this.scrollY.style.display = this.height > f ? _$_43fc[3] : _$_43fc[40]
                    } else this.scrollX.style.display = _$_43fc[40], this.scrollY.style.display = _$_43fc[40]
                };
            b.refresh = function() {
                this.scrollY.scrollTop = 0;
                this.scrollX.scrollLeft = 0;
                b.call(this)
            };
            b.updateX = function() {
                if (b.event) return !1;
                var d = parseInt(this.thead.lastChild.children[(this.options.freezeColumns || 0) + 1].getAttribute(_$_43fc[42]));
                b.setX.call(this, b.width.call(this, d));
                a.call(this)
            };
            b.updateY =
                function() {
                    if (b.event) return !1;
                    if (this.tbody.firstChild) {
                        var d = parseInt(this.tbody.firstChild.getAttribute(_$_43fc[43]));
                        b.setY.call(this, b.height.call(this, d));
                        c.call(this)
                    }
                };
            b.setX = function(d) {
                b.ignore = !0;
                this.scrollX.scrollLeft = d
            };
            b.setY = function(d) {
                b.ignore = !0;
                this.scrollY.scrollTop = d
            };
            b.update = function(d, e) {
                e == _$_43fc[45] ? (d = b.width.call(this, null, d.target.scrollLeft / (d.target.scrollWidth - this.content.offsetWidth) * this.width), C.reset.call(this), C[_$_43fc[46]].call(this, null, d), a.call(this)) :
                    (d = b.height.call(this, null, d.target.scrollTop / (d.target.scrollHeight - this.content.offsetHeight) * (this.height - (this.tbody.offsetHeight - 5))), this.tbody.textContent = _$_43fc[3], C[_$_43fc[46]].call(this, d), c.call(this))
            };
            b.build = function(d) {
                var e = this,
                    f = document.createElement(_$_43fc[20]),
                    g = document.createElement(_$_43fc[20]);
                f.className = _$_43fc[47] + d;
                g.className = _$_43fc[48] + d;
                f.appendChild(g);
                d == _$_43fc[45] ? f.style.height = _$_43fc[22] : f.style.width = _$_43fc[22];
                f.onscroll = function(h) {
                    if (b.ignore) return b.ignore = !1;
                    b.event = !0;
                    b.update.call(e, h, d);
                    b.event = !1
                };
                return f
            };
            b.width = function(d, e) {
                for (var f = this.options.columns, g = 0, h = this.options.freezeColumns || 0; h < this.options.columns.length; h++) {
                    if (d === h) return g;
                    !1 !== f[h].visible && f[h].type !== _$_43fc[49] && (g += parseInt(f[h].width));
                    if (g >= e) return h
                }
                return e ? h : g
            };
            b.height = function(d, e) {
                var f = 0,
                    g = 0;
                this.options.defaultRowHeight || (this.options.defaultRowHeight = 26);
                for (var h = this.results ? this.results : this.rows, l = 0; l < h.length; l++) {
                    g = l;
                    this.results && (g = h[l]);
                    if (d ===
                        g) return f;
                    this.rows[g] && !1 !== this.rows[g].visible && (f = this.rows[g].height ? f + parseInt(this.rows[g].height) : f + parseInt(this.options.defaultRowHeight));
                    if (f >= e) return g
                }
                return e ? g : f
            };
            return b
        }(),
        C = function() {
            var k = function() {
                var a = 0;
                this.results && (a = this.results[0]);
                k.reset.call(this, !0);
                k.renderX.call(this, 0);
                k.renderY.call(this, a);
                ia.headers.call(this);
                0 < this.options.pagination && qa.update.call(this);
                T.update.call(this);
                this.refreshBorders();
                P.call(this)
            };
            k.limited = function() {
                return 1 == this.options.tableOverflow ||
                    1 == this.parent.config.fullscreen
            };
            k.refresh = function() {
                if (!this.tbody.lastChild) return !1;
                var a = parseInt(this.thead.lastChild.lastChild.getAttribute(_$_43fc[42])) + 1,
                    c = parseInt(this.tbody.lastChild.getAttribute(_$_43fc[43]));
                this.results ? (c = this.results.indexOf(c), c < this.results.length - 1 && c++, c = this.results[c]) : c++;
                k.renderX.call(this, a);
                k.renderY.call(this, c);
                k.adjustX.call(this, !1);
                k.adjustY.call(this, !1);
                P.call(this)
            };
            k.renderX = function(a, c, b) {
                var d = this.options.columns,
                    e = 0,
                    f = [],
                    g = (this.options.freezeColumns ||
                        0) + 1;
                1 == this.parent.config.fullscreen ? e = this.content.offsetWidth : 1 == this.options.tableOverflow && (e = parseInt(this.options.tableWidth));
                var h = this.headerContainer.offsetWidth || 0;
                for (b || (b = 0); d[a] && (!e || h < e || 0 < b);) {
                    !1 !== d[a].visible && d[a].type !== _$_43fc[49] && (h += parseInt(d[a].width));
                    0 < b && b--;
                    var l = X.getWidth.call(this, a, f);
                    l > b && (b = l);
                    c ? (k.renderColumn.call(this, a, g), a--) : (k.renderColumn.call(this, a), a++)
                }
                X.batch.call(this, f)
            };
            k.renderY = function(a, c, b) {
                var d = 0,
                    e = [];
                1 == this.parent.config.fullscreen ? d =
                    this.content.offsetHeight : 1 == this.options.tableOverflow && (d = parseInt(this.options.tableHeight));
                d && (d -= this.thead.offsetHeight + 4, this.tfoot.offsetHeight && (d -= this.tfoot.offsetHeight + 14));
                var f = this.tbody.offsetHeight;
                var g = this.options.freezeColumns || 0,
                    h = parseInt(this.headerContainer.lastChild.getAttribute(_$_43fc[42]));
                g = this.headerContainer.children[g + 1] ? parseInt(this.headerContainer.children[g + 1].getAttribute(_$_43fc[42])) : h;
                var l = this.results ? this.results.indexOf(a) : a,
                    n = parseInt(this.options.pagination) ||
                    0,
                    q = this.tbody.children.length || 0,
                    p = !1;
                for (b || (b = 0); this.options.data && this.options.data[a] && (!n || q < n || 0 < b) && (!d || f <= d || 0 < b);) {
                    var r = k.renderRow.call(this, a, g, h);
                    c ? (this.tbody.insertBefore(r, this.tbody.firstChild), l--) : (this.tbody.appendChild(r), l++);
                    this.rows[a].height ? (r.offsetHeight !== this.rows[a].height && (this.rows[a].height = r.offsetHeight, p = !0), f += this.rows[a].height) : (f += this.rows[a].height = r.offsetHeight, p = !0);
                    0 < b && b--;
                    a = X.getHeight.call(this, a, e);
                    a > b && (b = a);
                    a = this.results ? this.results[l] :
                        l;
                    q++
                }
                X.batch.call(this, e);
                p && P.call(this)
            };
            k.renderColumn = function(a, c, b) {
                this.headers[a] || ya.create.call(this, a);
                if (this.options.nestedHeaders)
                    for (b = 0; b < this.options.nestedHeaders.length; b++) {
                        var d = la.getColumns.call(this, b);
                        void 0 !== d[a] && la.renderCell.call(this, d[a], b, c)
                    }
                if (this.options.footers)
                    for (b = 0; b < this.options.footers.length; b++) d = W.create.call(this, a, b), null === c ? this.tfoot.children[b].appendChild(d) : this.tfoot.children[b].insertBefore(d, this.tfoot.children[b].children[c]);
                null === c ? (this.headerContainer.appendChild(this.headers[a]),
                    this.colgroupContainer.appendChild(this.colgroup[a])) : (this.headerContainer.insertBefore(this.headers[a], this.headerContainer.children[c]), this.colgroupContainer.insertBefore(this.colgroup[a], this.colgroupContainer.children[c]));
                if (this.tbody.children.length)
                    for (b = 0; b < this.tbody.children.length; b++) d = parseInt(this.tbody.children[b].getAttribute(_$_43fc[43])), (d = I[_$_43fc[51]].call(this, a, d)) && (null === c ? this.tbody.children[b].appendChild(d) : this.tbody.children[b].insertBefore(d, this.tbody.children[b].children[c]))
            };
            k.renderRow = function(a, c, b, d) {
                var e = this.options.freezeColumns || 0;
                null == c && (c = parseInt(this.headerContainer.children[e + 1].getAttribute(_$_43fc[42])));
                null == b && (b = parseInt(this.headerContainer.lastChild.getAttribute(_$_43fc[42])));
                var f = wa.create.call(this, a, d);
                if (f.children.length)
                    for (; f.children[e + 1];) f.removeChild(f.lastChild);
                if (0 < e) {
                    for (var g = 0; g < e; g++) f.appendChild(I[_$_43fc[51]].call(this, g, a));
                    ia.update.call(this, a)
                }
                for (g = c; g <= b; g++) c = I[_$_43fc[51]].call(this, g, a, d), f.appendChild(c);
                return f
            };
            k.resetY = function() {
                if (this.options.pagination) {
                    this.pageNumber || (this.pageNumber = 0);
                    var a = this.options.pagination * this.pageNumber
                } else a = 0;
                var c = 0;
                if (this.merged.rows[a]) {
                    for (c = this.options.pagination; this.merged.rows[a] && 0 <= a;) a--;
                    a++
                }
                this.tbody.textContent = _$_43fc[3];
                this.results && this.results.length && (a = this.results[a]);
                (!this.results || 0 < this.results.length) && C.renderY.call(this, a, 0, c);
                0 < this.options.pagination && qa.update.call(this);
                P.call(this);
                this.refreshBorders()
            };
            k[_$_43fc[46]] = function(a,
                c) {
                if (null !== a && 0 <= a)
                    if (0 < this.options.pagination) this.page(this.whichPage(a));
                    else if (this.rows[a] || (a = this.options.data.length - 1), !wa.attached.call(this, a)) {
                    if (this.merged.rows[a]) {
                        for (; this.merged.rows[a] && 0 <= a;) a--;
                        a++
                    }
                    this.tbody.textContent = _$_43fc[3];
                    k.renderY.call(this, a);
                    var b = this.results ? this.results.indexOf(a) : a;
                    0 < b && (this.results ? b = this.results[b - 1] : b--, k.renderY.call(this, b, !0));
                    P.updateY.call(this)
                }
                if (null !== c && 0 <= c && (this.options.columns[c] || (c = this.options.columns.length - 1), a || (a =
                        k.getY.call(this)), !I.attached.call(this, c, a))) {
                    if (this.merged.cols[c]) {
                        for (b = a = 0; b < this.rows.length; b++) this.records[b][c].merged && (a = this.records[b][c].merged[0]);
                        c -= a
                    }
                    k.reset.call(this);
                    k.renderX.call(this, c);
                    0 < c && k.renderX.call(this, c - 1, !0);
                    P.updateX.call(this)
                }
                this.refreshBorders()
            };
            k.pageUp = function(a, c, b) {
                var d = parseInt(this.tbody.firstChild.getAttribute(_$_43fc[43]));
                if (c) {
                    a = null;
                    var e = this.results ? this.results[0] : 0
                } else this.results ? (e = this.results.indexOf(d), 0 < e && e--, e = this.results[e]) :
                    (e = d, 0 < e && e--);
                if (e < d) {
                    if (null == a) this.tbody.textContent = _$_43fc[3];
                    else {
                        d = a;
                        if (this.options.mergeCells) {
                            var f = parseInt(this.tbody.lastChild.getAttribute(_$_43fc[43]));
                            d += X.getHeight.call(this, f)
                        }
                        for (f = 0; f < d; f++) this.tbody.removeChild(this.tbody.lastChild)
                    }
                    k.renderY.call(this, e, !c, a);
                    k.adjustY.call(this, !1);
                    b && b.preventDefault();
                    P.updateY.call(this)
                }
                this.refreshBorders()
            };
            k.pageDown = function(a, c, b) {
                var d = parseInt(this.tbody.lastChild.getAttribute(_$_43fc[43]));
                if (c) {
                    a = null;
                    var e = this.results ? this.results[this.results.length -
                        1] : this.rows.length - 1
                } else this.results ? (e = this.results.indexOf(d), e < this.results.length - 1 && e++, e = this.results[e]) : (e = d, d < this.options.data.length - 1 && e++);
                if (e > d) {
                    if (null == a) this.tbody.textContent = _$_43fc[3];
                    else {
                        d = a;
                        if (this.options.mergeCells) {
                            var f = parseInt(this.tbody.firstChild.getAttribute(_$_43fc[43]));
                            d += X.getHeight.call(this, f)
                        }
                        for (f = 0; f < d; f++) this.tbody.removeChild(this.tbody.firstChild)
                    }
                    k.renderY.call(this, e, c, a);
                    k.adjustY.call(this, !0);
                    b && b.preventDefault();
                    P.updateY.call(this)
                }
                this.refreshBorders()
            };
            k.removeColumns = function(a, c) {
                var b = null === c ? this.headerContainer.lastChild : this.headerContainer.children[c];
                if (b) b = parseInt(b.getAttribute(_$_43fc[42]));
                else return !1;
                if (this.options.mergeCells) {
                    for (var d = 0, e = b, f = 0; f < a; f++) {
                        var g = X.getWidth.call(this, e);
                        g > d && (d = g);
                        null === c ? e-- : e++
                    }
                    a += d
                }
                a >= this.headerContainer.children.length && (a = this.headerContainer.children.length - 1);
                if ((d = this.options.nestedHeaders) && d.length)
                    for (e = 0; e < d.length; e++)
                        for (g = la.getColumns.call(this, e), f = 0; f < a; f++)
                            if (void 0 !== g[b + f] &&
                                this.nested.content[e][g[b + f]]) {
                                var h = this.nested.content[e][g[b + f]].element,
                                    l = h.getAttribute(_$_43fc[52]);
                                0 < l && l--;
                                0 < l ? h.setAttribute(_$_43fc[52], l) : h.parentNode && this.thead.children[e].removeChild(h)
                            } for (f = 0; f < a; f++) null === c ? (this.headerContainer.removeChild(this.headerContainer.lastChild), this.colgroupContainer.removeChild(this.colgroupContainer.lastChild)) : this.headerContainer.children[c] && (this.headerContainer.removeChild(this.headerContainer.children[c]), this.colgroupContainer.removeChild(this.colgroupContainer.children[c]));
                for (e = 0; e < this.tbody.children.length; e++)
                    for (f = 0; f < a; f++) null === c ? this.tbody.children[e].removeChild(this.tbody.children[e].lastChild) : this.tbody.children[e].children[c] && this.tbody.children[e].removeChild(this.tbody.children[e].children[c]);
                for (e = 0; e < this.tfoot.children.length; e++)
                    for (f = 0; f < a; f++) null === c ? this.tfoot.children[e].removeChild(this.tfoot.children[e].lastChild) : this.tfoot.children[e].removeChild(this.tfoot.children[e].children[c])
            };
            k.pageLeft = function(a, c) {
                var b = this.options.freezeColumns ||
                    0;
                if (c) {
                    a = null;
                    var d = b
                } else d = parseInt(this.thead.lastChild.children[b + 1].getAttribute(_$_43fc[42])) - 1;
                d >= b && (null == a ? k.reset.call(this) : k.removeColumns.call(this, a, null), k.renderX.call(this, d, !c, a), k.adjustX.call(this, !1), P.updateX.call(this));
                this.refreshBorders()
            };
            k.pageRight = function(a, c) {
                var b = this.options.freezeColumns || 0;
                if (c) {
                    a = null;
                    var d = this.options.columns.length - 1
                } else d = parseInt(this.thead.lastChild.lastChild.getAttribute(_$_43fc[42])) + 1;
                d < this.options.columns.length && (null == a ? k.reset.call(this) :
                    k.removeColumns.call(this, a, b + 1), k.renderX.call(this, d, c, a), k.adjustX.call(this, !0), P.updateX.call(this));
                this.refreshBorders()
            };
            k.reset = function(a) {
                1 < this.headerContainer.children.length && (a = a ? 0 : this.options.freezeColumns || 0, k.removeColumns.call(this, this.headerContainer.children.length - (a + 1), a + 1))
            };
            k.getX = function() {
                if (this.thead.lastChild) return parseInt(this.thead.lastChild.children[1].getAttribute(_$_43fc[42]))
            };
            k.getY = function() {
                if (this.tbody.firstChild) return parseInt(this.tbody.firstChild.getAttribute(_$_43fc[43]))
            };
            k.adjustX = function(a, c) {
                var b = this.options.freezeColumns || 0;
                if (a) {
                    c = this.options.columns.length - 1;
                    var d = parseInt(this.tbody.firstChild.lastChild.getAttribute(_$_43fc[42]));
                    b = parseInt(this.tbody.firstChild.children[b + 1].getAttribute(_$_43fc[42]))
                } else c = b, d = parseInt(this.tbody.firstChild.children[b + 1].getAttribute(_$_43fc[42])), b = parseInt(this.tbody.firstChild.lastChild.getAttribute(_$_43fc[42]));
                d === c && (a ? b-- : b++, k.renderX.call(this, b, a))
            };
            k.adjustY = function(a, c) {
                if (a) {
                    c = this.rows.length - 1;
                    var b =
                        parseInt(this.tbody.lastChild.getAttribute(_$_43fc[43])),
                        d = parseInt(this.tbody.firstChild.getAttribute(_$_43fc[43]))
                } else c = 0, b = parseInt(this.tbody.firstChild.getAttribute(_$_43fc[43])), d = parseInt(this.tbody.lastChild.getAttribute(_$_43fc[43]));
                b == c && (this.results ? (c = this.results.indexOf(d), d = a ? this.results[c - 1] : this.results[c + 1]) : a ? d-- : d++, k.renderY.call(this, d, a))
            };
            k.isVisible = function(a, c) {
                var b = this.thead.clientHeight;
                c || (c = 1);
                var d = !1,
                    e = !1,
                    f = a.getAttribute(_$_43fc[42]),
                    g = a.getAttribute(_$_43fc[43]);
                var h = this.records[g][f];
                h.merged && (f -= h.merged[0], g -= h.merged[1], a = this.records[g][f].element);
                g = this.content.getBoundingClientRect();
                h = a.getBoundingClientRect();
                var l = ia.getWidth.call(this),
                    n = g.left + l,
                    q = g.left + g.width;
                f = g.top + b;
                var p = g.top + g.height;
                var r = 0;
                g = h.top;
                g >= f && g <= p || (r++, d = !0);
                g = h.top + h.height;
                g >= f && g <= p || r++;
                if (r >= c) return 2 > c && (c = this.content.scrollTop, 0 == d ? p - f > h.height && (this.content.scrollTop += g - p + 3, P.ignore = !0, this.scrollY.scrollTop += g - p + 3) : (this.content.scrollTop = a.offsetTop - b, P.ignore = !0, this.scrollY.scrollTop -= c - this.content.scrollTop)), !1;
                if (!l || !a.classList.contains(_$_43fc[50]))
                    if (r = 0, f = h.left, f >= n && f <= q || (r++, e = !0), f = h.left + h.width, f >= n && f < q || r++, r >= c) return 2 > c && (c = this.content.scrollLeft, 0 == e ? q - n > h.width && (this.content.scrollLeft += f - q + 3, P.ignore = !0, this.scrollX.scrollLeft += f - q + 3) : (this.content.scrollLeft = a.offsetLeft - n - 50, P.ignore = !0, this.scrollX.scrollLeft -= c - this.content.scrollLeft)), !1;
                return !0
            };
            return k
        }(),
        Ia = function(k, a, c, b) {
            for (var d = this.options.columns, e = k, f, g =
                    null; k < d.length - 1;)
                if (k++, !1 !== d[k].visible && d[k].type !== _$_43fc[49]) {
                    f = this.records[a] && this.records[a][k].v === _$_43fc[3] ? !1 : !0;
                    null === g && (g = f);
                    if (this.records[a] && this.records[a][k].element && this.records[a][k].element.style.display == _$_43fc[40]) {
                        if (this.records[a][k].merged) {
                            if (k == k + this.records[a][k].merged[0] && (e = k, !c)) break;
                            f = !1
                        }
                    } else {
                        if (c && f !== g && !b) break;
                        if (c) e = k;
                        else return k
                    }
                    g = f
                } return e
        },
        Ja = function(k, a, c, b) {
            for (var d = this.options.columns, e = k, f, g = null; 0 < k;)
                if (k--, !1 !== d[k].visible && d[k].type !==
                    _$_43fc[49]) {
                    f = this.records[a] && this.records[a][k].v === _$_43fc[3] ? !1 : !0;
                    null === g && (g = f);
                    if (this.records[a] && this.records[a][k].element && this.records[a][k].element.style.display == _$_43fc[40]) {
                        if (this.records[a][k].merged) {
                            e = k - this.records[a][k].merged[0];
                            if (!c) break;
                            f = !0
                        }
                    } else {
                        if (c && f !== g && !b) break;
                        if (c) e = k;
                        else return k
                    }
                    g = f
                } return e
        },
        Oa = function() {
            var k = function(c, b, d, e) {
                    1 == c || 3 == c ? wa.attached.call(this, d) || (0 < this.options.pagination ? this.page(this.whichPage(d)) : (1 == c ? C.pageUp.call(this, 1, e) : C.pageDown.call(this,
                        1, e), wa.attached.call(this, d) || C[_$_43fc[46]].call(this, d))) : I.attached.call(this, b, d) || (0 == c ? C.pageLeft.call(this, 1, e) : C.pageRight.call(this, 1, e), I.attached.call(this, b, d) || C[_$_43fc[46]].call(this, d, b))
                },
                a = function(c) {
                    c.up = a.up;
                    c.down = a.down;
                    c.right = a.right;
                    c.left = a.left;
                    c.last = a.last;
                    c.first = a.first
                };
            a.up = function(c, b, d) {
                if (c) var e = parseInt(this.selectedCell[2]),
                    f = parseInt(this.selectedCell[3]);
                else e = parseInt(this.selectedCell[0]), f = parseInt(this.selectedCell[1]);
                a: {
                    var g = e,
                        h = f,
                        l = h,
                        n = null;
                    if (this.results &&
                        (h = this.results.indexOf(h), -1 == h)) {
                        d = l;
                        break a
                    }
                    for (; 0 < h;) {
                        h--;
                        var q = this.results ? this.results[h] : h;
                        if (!1 !== this.rows[q].visible) {
                            var p = this.records[q][g].v === _$_43fc[3] ? !1 : !0;
                            null === n && (n = p);
                            if (this.records[q][g].element && this.records[q][g].element.style.display == _$_43fc[40]) {
                                if (this.records[q][g].merged) {
                                    l = q - this.records[q][g].merged[1];
                                    if (!b) break;
                                    p = !0
                                }
                            } else if (b && p !== n && !d) {
                                d = l;
                                break a
                            } else if (b) l = this.records[q][g].merged ? q - this.records[q][g].merged[1] : q;
                            else {
                                d = q;
                                break a
                            }
                            n = p
                        }
                    }
                    d = l
                }
                d != f && (f = d, c ?
                    (c = this.selectedCell[0], d = this.selectedCell[1]) : (c = e, d = f, e = c, f = d), k.call(this, 1, e, f, b), this.updateSelectionFromCoords(c, d, e, f))
            };
            a.down = function(c, b, d) {
                if (c) var e = parseInt(this.selectedCell[2]),
                    f = parseInt(this.selectedCell[3]);
                else e = parseInt(this.selectedCell[0]), f = parseInt(this.selectedCell[1]);
                a: {
                    var g = e,
                        h = f,
                        l = h,
                        n = null;
                    if (this.results) {
                        h = this.results.indexOf(h);
                        if (-1 == h) {
                            d = l;
                            break a
                        }
                        var q = this.results.length - 1
                    } else q = this.rows.length - 1;
                    for (; h < q;) {
                        h++;
                        var p = this.results ? this.results[h] : h;
                        if (!1 !==
                            this.rows[p].visible) {
                            var r = this.records[p][g].v === _$_43fc[3] ? !1 : !0;
                            null === n && (n = r);
                            if (this.records[p][g].element && this.records[p][g].element.style.display == _$_43fc[40]) {
                                if (this.records[p][g].merged) {
                                    if (!this.records[p][g].merged[1] && (l = p, !b)) break;
                                    r = !0
                                }
                            } else if (b && r !== n && !d) {
                                d = l;
                                break a
                            } else if (b) l = p;
                            else {
                                d = p;
                                break a
                            }
                            n = r
                        }
                    }
                    d = l
                }
                d != f && (f = d, c ? (c = this.selectedCell[0], d = this.selectedCell[1]) : (c = e, d = f, e = c, f = d), k.call(this, 3, e, f, b), this.updateSelectionFromCoords(c, d, e, f))
            };
            a.right = function(c, b, d) {
                if (c) var e =
                    parseInt(this.selectedCell[2]),
                    f = parseInt(this.selectedCell[3]);
                else e = parseInt(this.selectedCell[0]), f = parseInt(this.selectedCell[1]);
                d = Ia.call(this, e, f, b, d);
                d != e && (e = d, c ? (c = this.selectedCell[0], d = this.selectedCell[1]) : (c = e, d = f, e = c, f = d), k.call(this, 2, e, f, b), this.updateSelectionFromCoords(c, d, e, f))
            };
            a.left = function(c, b, d) {
                if (c) var e = parseInt(this.selectedCell[2]),
                    f = parseInt(this.selectedCell[3]);
                else e = parseInt(this.selectedCell[0]), f = parseInt(this.selectedCell[1]);
                d = Ja.call(this, e, f, b, d);
                d != e && (e =
                    d, c ? (c = this.selectedCell[0], d = this.selectedCell[1]) : (c = e, d = f, e = c, f = d), k.call(this, 0, e, f, b), this.updateSelectionFromCoords(c, d, e, f))
            };
            a.first = function(c, b) {
                b ? a.up.call(this, c, !0, !0) : a.left.call(this, c, !0, !0)
            };
            a.last = function(c, b) {
                b ? a.down.call(this, c, !0, !0) : a.right.call(this, c, !0, !0)
            };
            return a
        }(),
        Pa = function() {
            return function(k) {
                k.save = Ka;
                k.fullscreen = Ga;
                k.setReadOnly = Ca;
                k.getReadOnly = Ca;
                k.isReadOnly = Ca;
                k.setPlugins = function(a) {
                    ja.call(this.parent, a)
                };
                k.showIndex = function() {
                    va.call(this, !0)
                };
                k.hideIndex =
                    function() {
                        va.call(this, !1)
                    };
                k[_$_43fc[46]] = C[_$_43fc[46]];
                k.undo = function() {
                    Q.undo.call(k.parent)
                };
                k.redo = function() {
                    Q.redo.call(k.parent)
                };
                k.isEditable = function() {
                    return N.call(k.parent, k)
                };
                k.dispatch = function() {
                    return B.apply(this.parent, arguments)
                };
                k.setViewport = function(a, c) {
                    this.options.tableOverflow = !0;
                    a = parseInt(a);
                    c = parseInt(c);
                    100 < a && (this.options.tableWidth = a + _$_43fc[44], this.content.style.maxWidth = this.options.tableWidth);
                    100 < c && (this.options.tableHeight = parseInt(c) + _$_43fc[44], this.content.style.maxHeight =
                        this.options.tableHeight);
                    this.tbody.innerHTML = _$_43fc[3];
                    C.call(this);
                    P.refresh.call(this);
                    ca.update.call(this.parent, this)
                }
            }
        }(),
        za = function() {
            var k = {
                    logo: null,
                    url: null,
                    persistence: !1,
                    sequence: !0,
                    data: null,
                    json: null,
                    rows: [],
                    columns: [],
                    cells: {},
                    role: null,
                    nestedHeaders: null,
                    defaultColWidth: 100,
                    defaultRowHeight: 0,
                    defaultColAlign: _$_43fc[53],
                    minSpareRows: 0,
                    minSpareCols: 0,
                    minDimensions: [0, 0],
                    csv: null,
                    csvFileName: _$_43fc[36],
                    csvHeaders: !0,
                    csvDelimiter: _$_43fc[25],
                    columnSorting: !0,
                    columnDrag: !0,
                    columnResize: !0,
                    rowResize: !0,
                    rowDrag: !0,
                    editable: !0,
                    allowInsertRow: !0,
                    allowManualInsertRow: !0,
                    allowInsertColumn: !0,
                    allowManualInsertColumn: !0,
                    allowDeleteRow: !0,
                    allowDeletingAllRows: !1,
                    allowDeleteColumn: !0,
                    allowRenameColumn: !0,
                    allowComments: !0,
                    selectionCopy: !0,
                    mergeCells: {},
                    search: !1,
                    pagination: !1,
                    paginationOptions: null,
                    textOverflow: !1,
                    tableOverflow: !1,
                    tableHeight: _$_43fc[54],
                    tableWidth: null,
                    comments: null,
                    meta: null,
                    style: {},
                    freezeColumns: 0,
                    orderBy: null,
                    worksheetId: _$_43fc[3],
                    worksheetName: null,
                    worksheetState: null,
                    filters: !1,
                    footers: null,
                    validations: null,
                    formify: null,
                    applyMaskOnFooters: !1,
                    pluginOptions: null,
                    locked: !1,
                    selectUnLockedCells: !0,
                    selectLockedCells: !0
                },
                a = {
                    application: _$_43fc[55],
                    cloud: null,
                    root: null,
                    definedNames: {},
                    sorting: null,
                    server: null,
                    toolbar: null,
                    editable: !0,
                    allowExport: !0,
                    includeHeadersOnDownload: !1,
                    forceUpdateOnPaste: !1,
                    loadingSpin: !1,
                    fullscreen: !1,
                    secureFormulas: !0,
                    parseFormulas: !0,
                    debugFormulas: !1,
                    editorFormulas: !0,
                    autoIncrement: !0,
                    autoCasting: !0,
                    stripHTML: !1,
                    tabs: !1,
                    allowDeleteWorksheet: !0,
                    allowRenameWorksheet: !0,
                    allowMoveWorksheet: !0,
                    onevent: null,
                    onclick: null,
                    onload: null,
                    onundo: null,
                    onredo: null,
                    onbeforesave: null,
                    onsave: null,
                    onbeforechange: null,
                    onchange: null,
                    onafterchanges: null,
                    oncopy: null,
                    onbeforepaste: null,
                    onpaste: null,
                    onbeforeinsertrow: null,
                    oninsertrow: null,
                    onbeforedeleterow: null,
                    ondeleterow: null,
                    onbeforeinsertcolumn: null,
                    oninsertcolumn: null,
                    onbeforedeletecolumn: null,
                    ondeletecolumn: null,
                    onmoverow: null,
                    onmovecolumn: null,
                    onresizerow: null,
                    onresizecolumn: null,
                    onselection: null,
                    onbeforecomments: null,
                    oncomments: null,
                    onbeforesort: null,
                    onsort: null,
                    onfocus: null,
                    onblur: null,
                    onmerge: null,
                    onchangeheader: null,
                    onchangefooter: null,
                    onchangefootervalue: null,
                    onchangenested: null,
                    onchangenestedcell: null,
                    oncreateeditor: null,
                    oneditionstart: null,
                    oneditionend: null,
                    onchangestyle: null,
                    onchangemeta: null,
                    onbeforechangepage: null,
                    onchangepage: null,
                    onbeforecreateworksheet: null,
                    oncreateworksheet: null,
                    onrenameworksheet: null,
                    ondeleteworksheet: null,
                    onmoveworksheet: null,
                    onopenworksheet: null,
                    onchangerowid: null,
                    onbeforesearch: null,
                    onsearch: null,
                    onbeforefilter: null,
                    onfilter: null,
                    oncreatecell: null,
                    oncreaterow: null,
                    updateTable: null,
                    contextMenu: null,
                    parseTableFirstRowAsHeader: !1,
                    parseTableAutoCellType: !1,
                    plugins: null,
                    wordWrap: !0,
                    about: !0,
                    license: null,
                    worksheets: null
                },
                c = function(e, f) {
                    f = JSON.parse(JSON.stringify(f));
                    var g = {},
                        h;
                    for (h in f) e && e.hasOwnProperty(h) ? g[h] = e[h] : g[h] = f[h];
                    return g
                },
                b = function(e) {
                    e && (typeof e == _$_43fc[17] && (e = JSON.parse(e)), this.options.data = e);
                    this.options.json && (this.options.data = this.options.json);
                    this.options.data ||
                        (this.options.data = []);
                    e = this.options.columns.length;
                    if (this.options.data && typeof this.options.data[0] !== _$_43fc[13]) {
                        var f = this.options.data[0].data && 0 < Object.keys(this.options.data[0].data).length ? Object.keys(this.options.data[0].data) : Object.keys(this.options.data[0]);
                        f.length > e && (e = f.length)
                    }
                    this.options.minDimensions[0] > e && (e = this.options.minDimensions[0]);
                    e || (e = 8);
                    for (var g = 0; g < e; g++) this.options.columns[g] ? this.options.columns[g].type || (this.options.columns[g].type = _$_43fc[56]) : this.options.columns[g] = {
                            type: _$_43fc[56]
                        }, this.options.columns[g].type == _$_43fc[57] && (this.options.columns[g].type = _$_43fc[58], this.options.columns[g].autocomplete = !0), !this.options.columns[g].name && f && f[g] && Number(f[g]) != f[g] && (this.options.columns[g].name = f[g]), this.options.columns[g].width || (this.options.columns[g].width = this.options.defaultColWidth), this.options.columns[g].type != _$_43fc[58] || this.options.columns[g].source || (this.options.columns[g].source = []), this.options.columns[g].type == _$_43fc[58] && this.options.columns[g].url &&
                        jSuites.ajax({
                            url: this.options.columns[g].url,
                            index: g,
                            method: _$_43fc[59],
                            dataType: _$_43fc[60],
                            worksheet: this,
                            group: this.parent.name,
                            success: function(h) {
                                this.worksheet.options.columns[this.index].source = h
                            }
                        })
                },
                d = function(e) {
                    e.getConfig = d[_$_43fc[51]];
                    e.setConfig = d[_$_43fc[11]]
                };
            d.spreadsheet = function(e) {
                this.config = c(e, a);
                1 != this.config.secureFormulas && console.log(_$_43fc[61])
            };
            d.worksheet = function(e) {
                var f = this;
                f.options = c(e, k);
                f.options.csv ? jSuites.ajax({
                    url: f.options.csv,
                    method: _$_43fc[59],
                    dataType: _$_43fc[56],
                    group: f.parent.name,
                    success: function(g) {
                        g = A.parseCSV(g, f.options.csvDelimiter);
                        if (1 == f.options.csvHeaders && 0 < g.length)
                            for (var h = g.shift(), l = 0; l < h.length; l++) f.options.columns[l] || (f.options.columns[l] = {
                                type: _$_43fc[56],
                                align: f.options.defaultColAlign,
                                width: f.options.defaultColWidth
                            }), typeof f.options.columns[l].title === _$_43fc[13] && (f.options.columns[l].title = h[l]);
                        b.call(f, g)
                    }
                }) : f.options.url ? jSuites.ajax({
                    url: f.options.url,
                    method: _$_43fc[59],
                    dataType: _$_43fc[60],
                    group: f.parent.name,
                    success: function(g) {
                        b.call(f,
                            g)
                    }
                }) : b.call(f)
            };
            d[_$_43fc[51]] = function() {
                return this.options
            };
            d[_$_43fc[11]] = function(e) {
                typeof e == _$_43fc[17] && (e = JSON.parse(e));
                if (this.parent) {
                    f = Object.keys(e);
                    for (g = 0; g < f.length; g++) typeof k[f[g]] !== _$_43fc[13] && (this.options[f[g]] = e[f[g]]);
                    !0 === e.filters ? Y.show.call(this) : !1 === e.filters && Y.hide.call(this);
                    !0 === e.search ? ma.show.call(this) : !1 === e.search && ma.hide.call(this);
                    !0 === e.toolbar ? ca.show.call(this.parent) : !1 === e.toolbar && ca.hide.call(this.parent);
                    e.minDimensions && e.minDimensions[0] &&
                        (f = parseInt(e.minDimensions[0]) - this.headers.length, 0 < f && this.insertColumn(f), f = parseInt(e.minDimensions[1]) - this.rows.length, 0 < f && this.insertRow(f));
                    G.call(this, _$_43fc[62], {
                        data: JSON.stringify(e)
                    })
                } else
                    for (var f = Object.keys(e), g = 0; g < f.length; g++) typeof a[f[g]] !== _$_43fc[13] && (this.config[f[g]] = e[f[g]])
            };
            return d
        }(),
        Q = function(k) {
            k = function(a) {
                if (1 != this.ignoreHistory) {
                    var c = ++this.historyIndex;
                    this.history = this.history.slice(0, c + 1);
                    this.history[c] = a
                }
            };
            k.undo = function() {
                if (0 <= this.historyIndex) var a =
                    this.history[this.historyIndex--];
                if (a) {
                    this.ignoreHistory = !0;
                    var c = a.worksheet;
                    c.openWorksheet();
                    var b = [];
                    if (a.action == _$_43fc[63]) c.deleteRow(a.insertBefore ? a.rowNumber : a.rowNumber + 1, a.numOfRows);
                    else if (a.action == _$_43fc[64]) c.insertRow(a.numOfRows, a.rowNumber, 1, a.data), I.setAttributes.call(c, a.attributes);
                    else if (a.action == _$_43fc[65]) c.deleteColumn(a.insertBefore ? a.columnNumber : a.columnNumber + 1, a.numOfColumns);
                    else if (a.action == _$_43fc[66]) c.insertColumn(a.numOfColumns, a.columnNumber, 1, a.properties,
                        a.data, a.extra), I.setAttributes.call(c, a.attributes);
                    else if (a.action == _$_43fc[67]) c.moveRow(a.newValue, a.oldValue);
                    else if (a.action == _$_43fc[68]) c.moveColumn(a.newValue, a.oldValue);
                    else if (a.action == _$_43fc[69]) c.removeMerge(a.newValue, !0), c.setMerge(a.oldValue);
                    else if (a.action == _$_43fc[70]) c.setMerge(a.oldValue);
                    else if (a.action == _$_43fc[71]) c.setStyle(a.oldValue, null, null, 1);
                    else if (a.action == _$_43fc[72]) c.setStyle(a.oldValue);
                    else if (a.action == _$_43fc[73]) c.setWidth(a.column, a.oldValue);
                    else if (a.action ==
                        _$_43fc[74]) c.setHeight(a.row, a.oldValue);
                    else if (a.action == _$_43fc[75]) c.setHeader(a.column, a.oldValue);
                    else if (a.action == _$_43fc[76]) c.setComments(a.oldValue);
                    else if (a.action == _$_43fc[77]) c.setProperty(a.column, a.oldValue);
                    else if (a.action == _$_43fc[78]) c.orderBy(a.column, a.direction ? 0 : 1, a.oldValue);
                    else if (a.action == _$_43fc[79]) {
                        for (var d = 0; d < a.records.length; d++) {
                            var e = {
                                x: a.records[d].x,
                                y: a.records[d].y,
                                value: a.records[d].oldValue
                            };
                            a.records[d].oldStyle && (e.style = a.records[d].oldStyle);
                            b.push(e)
                        }
                        c.setValue(b)
                    } else a.action ==
                        _$_43fc[80] ? c.renameWorksheet(a.index, a.oldValue) : a.action == _$_43fc[81] && c.moveWorksheet(a.t, a.f);
                    a.selection && c.updateSelectionFromCoords(a.selection);
                    this.ignoreHistory = !1;
                    B.call(this, _$_43fc[82], c, a)
                }
            };
            k.redo = function() {
                if (this.historyIndex < this.history.length - 1) var a = this.history[++this.historyIndex];
                if (a) {
                    this.ignoreHistory = !0;
                    var c = a.worksheet;
                    c.openWorksheet();
                    a.action == _$_43fc[63] ? c.insertRow(a.numOfRows, a.rowNumber, a.insertBefore, a.data) : a.action == _$_43fc[64] ? c.deleteRow(a.rowNumber, a.numOfRows) :
                        a.action == _$_43fc[65] ? c.insertColumn(a.numOfColumns, a.columnNumber, a.insertBefore, a.properties, a.data) : a.action == _$_43fc[66] ? c.deleteColumn(a.columnNumber, a.numOfColumns) : a.action == _$_43fc[67] ? c.moveRow(a.oldValue, a.newValue) : a.action == _$_43fc[68] ? c.moveColumn(a.oldValue, a.newValue) : a.action == _$_43fc[69] ? c.setMerge(a.newValue) : a.action == _$_43fc[70] ? c.removeMerge(a.newValue) : a.action == _$_43fc[71] ? c.setStyle(a.newValue, null, null, 1) : a.action == _$_43fc[72] ? c.resetStyle(a.cells) : a.action == _$_43fc[73] ? c.setWidth(a.column,
                            a.newValue) : a.action == _$_43fc[74] ? c.setHeight(a.row, a.newValue) : a.action == _$_43fc[75] ? c.setHeader(a.column, a.newValue) : a.action == _$_43fc[76] ? c.setComments(a.newValue) : a.action == _$_43fc[77] ? c.setProperty(a.column, a.newValue) : a.action == _$_43fc[78] ? c.orderBy(a.column, a.direction, a.newValue) : a.action == _$_43fc[79] ? c.setValue(a.records) : a.action == _$_43fc[80] ? c.renameWorksheet(a.index, a.newValue) : a.action == _$_43fc[81] && c.moveWorksheet(a.f, a.t);
                    a.selection && c.updateSelectionFromCoords(a.selection);
                    this.ignoreHistory = !1;
                    B.call(this, _$_43fc[83], c, a)
                }
            };
            return k
        }(),
        ja = function() {
            var k = function(a) {
                var c = null;
                if (Array.isArray(a)) c = a;
                else if (typeof a == _$_43fc[84]) {
                    c = [];
                    var b = Object.keys(a);
                    for (var d = 0; d < b.length; d++) c.push({
                        name: b[d],
                        plugin: a[b[d]],
                        options: {}
                    })
                }
                if (c && c.length)
                    for (d = 0; d < c.length; d++) {
                        if (c[d].name && c[d].plugin) {
                            var e = c[d].name;
                            var f = c[d].plugin;
                            var g = c[d].options
                        }
                        typeof f === _$_43fc[85] && (this.plugins[e] = f.call(t, this, g, this.config), typeof f.license == _$_43fc[85] && t.license && f.license(t.license))
                    }
            };
            k.execute =
                function(a, c) {
                    if (this.plugins) {
                        var b = Object.keys(this.plugins);
                        if (b.length)
                            for (var d = 0; d < b.length; d++)
                                if (typeof this.plugins[b[d]][a] == _$_43fc[85]) {
                                    var e = this.plugins[b[d]][a].apply(this.plugins[b[d]], c);
                                    e && (a == _$_43fc[86] && (c[4] = e), a == _$_43fc[87] && (c[0] = e))
                                } return c
                    }
                };
            return k
        }(),
        Z = function() {
            var k = function(a) {
                a.getEditor = k[_$_43fc[51]];
                a.openEditor = k.open;
                a.closeEditor = k.close
            };
            k[_$_43fc[51]] = function(a, c) {
                a = Da.getOptions.call(this, a, c);
                return [a.type && typeof a.type == _$_43fc[84] ? a.type : t.editors[a.type],
                    a
                ]
            };
            k.open = function(a, c, b) {
                if (!N.call(this.parent, this)) return !1;
                if (1 != a.classList.contains(_$_43fc[18])) {
                    var d = parseInt(a.getAttribute(_$_43fc[42])),
                        e = parseInt(a.getAttribute(_$_43fc[43]));
                    if (!0 === this.options.locked) {
                        var f = A.getColumnNameFromCoords(d, e);
                        if (!this.options.cells || !this.options.cells[f] || !1 !== this.options.cells[f].locked) return !1
                    }
                    if (f = this.records[e][d].merged) d -= f[0], e -= f[1], a = this.records[e][d].element;
                    if (!I.attached.call(this, d, e)) {
                        if (this.results && 0 < this.results.length) return console.error(_$_43fc[88]),
                            !1;
                        C[_$_43fc[46]].call(this, e, d)
                    }
                    k.position.call(this, d, e);
                    f = this.parent.input;
                    f.innerText = _$_43fc[3];
                    B.call(this.parent, _$_43fc[89], this, a, d, e);
                    c = 1 == c ? _$_43fc[3] : K.call(this, d, e);
                    var g = k[_$_43fc[51]].call(this, d, e),
                        h = !0;
                    typeof g[0].openEditor == _$_43fc[85] && (!1 === g[0].openEditor(a, c, d, e, this, g[1], b) ? h = !1 : B.call(this.parent, _$_43fc[90], this, a, d, e, f, g[1]));
                    h && (1 == this.options.textOverflow && (this.records[e][d].element.style.overflow = _$_43fc[49], 0 < d && (this.records[e][d - 1].element.style.overflow = _$_43fc[49])),
                        this.edition = a, f.classList.add(_$_43fc[91]), 1 == this.parent.config.editorFormulas && M(c) && (f.classList.add(_$_43fc[92]), fa.parse.call(this, f), a = document.createTextNode(_$_43fc[3]), f.appendChild(a), jSuites.focus(f)))
                }
            };
            k.close = function(a, c) {
                null === a && (a = this.edition);
                this.parent.input.classList.contains(_$_43fc[92]) && fa.close.call(this, this.parent.input);
                var b, d = parseInt(a.getAttribute(_$_43fc[42])),
                    e = parseInt(a.getAttribute(_$_43fc[43]));
                1 == this.options.textOverflow && (b = this.records[e][d + 1]) && b.element &&
                    b.element.innerText == _$_43fc[3] && (this.records[e][d].element.style.overflow = _$_43fc[3]);
                b = k[_$_43fc[51]].call(this, d, e);
                if (1 == c) {
                    var f = null;
                    if (typeof b[0].closeEditor == _$_43fc[85]) {
                        var g = b[0].closeEditor(a, !0, d, e, this, b[1]);
                        void 0 !== g && (f = g)
                    }
                    null !== f && K.call(this, d, e) != f && this.setValue(a, f)
                } else b[0].closeEditor(a, !1, d, e, this, b[1]);
                B.call(this.parent, _$_43fc[93], this, a, d, e, f, c);
                b[1] && typeof b[1].render == _$_43fc[85] && b[1].render.call(this, a, this.records[e][d].v, d, e, this, b[1]);
                b = this.parent.input;
                b.onblur &&
                    (b.onblur = null);
                b.children[0] && b.children[0].onblur && (b.children[0].onblur = null);
                b.update && (b.update = null);
                Z.build(this.parent);
                b.removeAttribute(_$_43fc[94]);
                b.removeAttribute(_$_43fc[95]);
                b.removeAttribute(_$_43fc[96]);
                this.edition = b.mask = null;
                this.refreshBorders()
            };
            k.position = function(a, c) {
                var b = null;
                if (b = this.records[c][a].element) {
                    var d = this.parent.element.getBoundingClientRect();
                    b = b.getBoundingClientRect();
                    var e = this.parent.input;
                    e.x = a;
                    e.y = c;
                    0 == b.width && 0 == b.height ? e.style.opacity = _$_43fc[97] :
                        (e.setAttribute(_$_43fc[98], _$_43fc[3]), a = b.top - d.top + 1, c = b.left - d.left + 1, e.style.top = a + _$_43fc[44], e.style.left = c + _$_43fc[44], e.style.minWidth = b.width - 1 + _$_43fc[44], e.style.minHeight = b.height - 1 + _$_43fc[44], setTimeout(function() {
                            e.focus()
                        }, 0))
                }
            };
            k.build = function(a) {
                a.input || (a.input = document.createElement(_$_43fc[20]));
                a.input.className = _$_43fc[99];
                a.input.setAttribute(_$_43fc[100], !0);
                a.input.innerText = _$_43fc[3];
                a.input.oninput = function(c) {
                    t.current.parent.config.editorFormulas = !0;
                    M(c.target.innerText) ?
                        c.target.classList.add(_$_43fc[92]) : c.target.classList.contains(_$_43fc[92]) && fa.close.call(t.current, this)
                };
                return a.input
            };
            return k
        }(),
        B = function(k) {
            if (!this.ignoreEvents) {
                if (typeof this.config.onevent == _$_43fc[85]) var a = this.config.onevent.apply(this, arguments);
                typeof this.config[k] == _$_43fc[85] && (a = this.config[k].apply(this, Array.prototype.slice.call(arguments, 1)));
                ja.execute.call(this, _$_43fc[101], arguments)
            }
            return a
        },
        pa = function() {
            var k = function(a) {
                a.sequence = 0;
                a.getNextSequence = k.next;
                a.getRowId =
                    k.getId;
                a.setRowId = k.setId;
                a.getRowById = k.getRowById
            };
            k[_$_43fc[51]] = function() {
                return this.sequence
            };
            k[_$_43fc[11]] = function(a, c) {
                if (a > this.sequence || c) this.sequence = a
            };
            k.next = function() {
                return 1 == this.options.sequence ? ++this.sequence : null
            };
            k.reset = function() {
                this.sequence = 0
            };
            k.getId = function(a) {
                if (this.rows[a] && this.rows[a].id) return this.rows[a].id;
                var c = this.getPrimaryKey();
                if (!1 !== c) return K.call(this, c, a)
            };
            k.setId = function(a, c) {
                if (void 0 !== c && 0 <= parseInt(a)) {
                    var b = {};
                    b[a] = c
                } else b = a;
                a = Object.keys(b);
                if (a.length) {
                    for (var d = 0; d < a.length; d++) c = b[a[d]], this.rows[a[d]].id = c, k[_$_43fc[11]].call(this, c);
                    G.call(this, _$_43fc[102], [b]);
                    B.call(this.parent, _$_43fc[103], this, b)
                }
            };
            k.getRowById = function(a, c) {
                for (var b = 0; b < this.rows.length; b++)
                    if (k.getId.call(this, b) == a) return !0 === c ? this.rows[b] : this.options.data[b];
                return !1
            };
            return k
        }(),
        Ka = function() {
            var k = function(c) {
                    for (var b = [], d = {}, e = 0; e < c.length; e++) {
                        var f = c[e].x,
                            g = c[e].y,
                            h = pa.getId.call(this, g);
                        if (!h)
                            if (d[g]) h = d[g];
                            else if (h = pa.next.call(this)) d[g] = h;
                        b[g] || (b[g] = {
                            id: h,
                            row: g,
                            data: {}
                        });
                        f = V.call(this, f);
                        b[g].data[f] = c[e].value
                    }
                    pa.setId.call(this, d);
                    return b.filter(function(l) {
                        return null != l
                    })
                },
                a = function(c) {
                    var b = c[0];
                    if (0 <= [_$_43fc[104], _$_43fc[105], _$_43fc[102], _$_43fc[106]].indexOf(b)) return !1;
                    var d = {};
                    b == _$_43fc[79] || b == _$_43fc[107] ? (b = _$_43fc[108], d[b] = k.call(this, c[1].data)) : Array.isArray(c[1]) && 1 == c[1].length ? d[b] = c[1][0] : d[b] = c[1];
                    return d
                };
            return function(c, b, d, e) {
                var f = this.parent,
                    g = this;
                d = null;
                var h = a.call(this, b);
                if (h) b = h;
                else if (!1 ===
                    h) return !1;
                if (h = B.call(f, _$_43fc[109], f, g, b)) b = h;
                else if (!1 === h) return !1;
                Ha.call(f, !0);
                jSuites.ajax({
                    url: c,
                    method: _$_43fc[110],
                    dataType: _$_43fc[60],
                    data: {
                        data: JSON.stringify(b)
                    },
                    queue: !0,
                    beforeSend: function(l) {
                        d && l.setRequestHeader(_$_43fc[111], _$_43fc[112] + d)
                    },
                    success: function(l) {
                        f.element.classList.contains(_$_43fc[113]) && f.element.classList.remove(_$_43fc[113]);
                        l && (l.message && (l.name = f.config.application, jSuites.notification.isVisible() ? console.log(l.name + _$_43fc[114] + l.message) : jSuites.notification(l)),
                            l.success ? typeof e === _$_43fc[85] ? e(l) : l.data && Array.isArray(l.data) && I.updateAll.call(g, l.data) : l.error && (alert(_$_43fc[115]), window.open(window.location.href, _$_43fc[116])));
                        B.call(f, _$_43fc[117], f, g, b, l)
                    },
                    error: function() {
                        f.element.classList.add(_$_43fc[113])
                    }
                })
            }
        }(),
        G = function() {
            return function() {
                ja.execute.call(this.parent, _$_43fc[118], [this, arguments[0], arguments[1]]);
                if (!this.parent.ignorePersistence) {
                    var k = null;
                    if (this.parent.config.server) {
                        var a = this.parent.worksheets.indexOf(this);
                        k = this.parent.config.server;
                        k = -1 == k.indexOf(_$_43fc[119]) ? k + (_$_43fc[120] + a) : k + (_$_43fc[121] + a)
                    } else this.options.persistence && (k = typeof this.options.persistence == _$_43fc[17] ? this.options.persistence : this.options.url);
                    k && Ka.call(this, k, arguments)
                }
            }
        }(),
        Aa = function(k) {
            if (this.selectedCell) {
                var a = this.selectedCell[0],
                    c = this.selectedCell[1];
                this.records && this.records[c] && this.records[c][a] && this.records[c][a].element || (this.selectedCell = this.getHighlighted(), a = this.selectedCell[0], c = this.selectedCell[1]);
                var b = this.getHighlighted(),
                    d = b[0],
                    e = b[1],
                    f = b[2];
                b = b[3];
                k = k ? _$_43fc[122] : _$_43fc[123];
                if (this.records[c] && this.records[c][a] && this.records[c][a].element) this.records[c][a].element.classList[k](_$_43fc[124]);
                for (a = d; a <= f; a++)
                    if (this.headers[a]) this.headers[a].classList[k](_$_43fc[125]);
                for (; e <= b; e++)
                    if (this.rows[e] && this.rows[e].element) this.rows[e].element.classList[k](_$_43fc[125])
            }
        },
        ua = function() {
            var k = function(a) {
                a.resetSelection = k.reset;
                a.updateSelection = k.fromElements;
                a.updateSelectionFromCoords = k[_$_43fc[11]];
                a.selectAll =
                    k.all;
                a.isSelected = k.isSelected;
                a.getHighlighted = k.getHighlighted;
                a.getRange = k.getRange
            };
            k.getHighlighted = function() {
                var a = this.borders;
                return (a = a.main) ? [a.x1, a.y1, a.x2, a.y2] : null
            };
            k.isSelected = function(a, c, b) {
                if (!b && (b = this.getHighlighted(), !b)) return !1;
                var d = b[0],
                    e = b[1],
                    f = b[2];
                b = b[3];
                f || (f = d);
                b || (b = e);
                return null == a ? c >= e && c <= b : null == c ? a >= d && a <= f : a >= d && a <= f && c >= e && c <= b
            };
            k.reset = function() {
                if (this.selectedCell) a = 1, this.edition && Z.close.call(this, this.edition, !0), this.resetBorders(_$_43fc[126], !0),
                    this.resetBorders(_$_43fc[127], !0), this.selectedCell = null, 1 == a && B.call(this.parent, _$_43fc[128], this);
                else var a = 0;
                return a
            };
            k.refresh = function() {
                this.selectedCell && k[_$_43fc[11]].call(this, this.selectedCell)
            };
            k.isValid = function(a, c, b, d) {
                return !(a >= this.options.columns.length || c >= this.rows.length || b >= this.options.columns.length || d >= this.rows.length)
            };
            k[_$_43fc[11]] = function(a, c, b, d, e, f, g) {
                f || (f = _$_43fc[126]);
                Array.isArray(a) && (d = a[3], b = a[2], c = a[1], a = a[0]);
                null == b && (b = a);
                null == d && (d = c);
                if (!k.isValid.call(this,
                        a, c, b, d)) return !1;
                if (null != a) {
                    if (parseInt(a) < parseInt(b)) var h = parseInt(a),
                        l = parseInt(b);
                    else h = parseInt(b), l = parseInt(a);
                    if (parseInt(c) < parseInt(d)) var n = parseInt(c),
                        q = parseInt(d);
                    else n = parseInt(d), q = parseInt(c);
                    var p;
                    var r = {};
                    if (this.options.mergeCells) {
                        for (var w = n; w <= q; w++)
                            for (var m = h; m <= l; m++)
                                if (p = this.records[w][m].merged) p = A.getColumnNameFromCoords(m - p[0], w - p[1]), r[p] = !0;
                        w = Object.keys(r);
                        if (w.length)
                            for (m = 0; m < w.length; m++) p = A.getCoordsFromColumnName(w[m]), r = this.options.mergeCells[w[m]], p[0] <
                                h && (h = p[0]), p[0] + r[0] - 1 > l && (l = p[0] + r[0] - 1), p[1] < n && (n = p[1]), p[1] + r[1] - 1 > q && (q = p[1] + r[1] - 1)
                    }
                }
                f == _$_43fc[126] && (this.selectedCell ? (Aa.call(this, !1), p = 1) : p = 0, this.selectedCell = [a, c, b, d], this.records[d][b].element && C.isVisible.call(this, this.records[d][b].element), Z.position.call(this, a, c), 0 == p && B.call(this.parent, _$_43fc[129], this));
                da[_$_43fc[11]].call(this, h, n, l, q, f, g);
                f == _$_43fc[126] && (ja.execute.call(this.parent, _$_43fc[130], [this, h, n, l, q, e]), B.call(this.parent, _$_43fc[131], this, h, n, l, q, e), ca.update.call(this.parent,
                    t.current, this.selectedCell))
            };
            k.fromElements = function(a, c, b) {
                var d = a.getAttribute(_$_43fc[42]);
                a = a.getAttribute(_$_43fc[43]);
                if (c) {
                    var e = c.getAttribute(_$_43fc[42]);
                    c = c.getAttribute(_$_43fc[43])
                } else e = d, c = a;
                this.updateSelectionFromCoords(d, a, e, c, b)
            };
            k.all = function() {
                k[_$_43fc[11]].call(this, 0, 0, this.options.columns.length - 1, this.records.length - 1)
            };
            k.getRange = function() {
                var a = this.selectedCell;
                if (!a) return _$_43fc[3];
                var c = A.getColumnNameFromCoords(a[0], a[1]);
                a = A.getColumnNameFromCoords(a[2], a[3]);
                var b = this.options.worksheetName;
                b = -1 != b.indexOf(_$_43fc[132]) ? _$_43fc[133] + b + _$_43fc[134] : b + _$_43fc[135];
                return c == a ? b + c : b + t.helpers.getRangeFromTokens(t.helpers.getTokensFromRange(c + _$_43fc[136] + a))
            };
            return k
        }(),
        da = function() {
            var k = function(b) {
                b.borders = [];
                b.setBorder = k[_$_43fc[11]];
                b.getBorder = k[_$_43fc[51]];
                b.resetBorders = k.reset;
                b.refreshBorders = k.refresh
            };
            k[_$_43fc[51]] = function(b) {
                return this.borders[b]
            };
            var a = function(b, d, e, f, g, h) {
                    g || (g = 0);
                    if (!ua.isValid.call(this, b, d, e, f)) return !1;
                    if (this.borders[g]) l =
                        this.borders[g];
                    else {
                        var l = {};
                        l.element = document.createElement(_$_43fc[20]);
                        l.element.classList.add(_$_43fc[137]);
                        g == _$_43fc[127] || g == _$_43fc[138] || g == _$_43fc[126] ? l.element.classList.add(_$_43fc[139] + g) : (h = h ? h : jSuites.randomColor(!0), l.color = h, l.element.style.backgroundColor = h + _$_43fc[140], l.element.style.borderColor = h);
                        this.content.appendChild(l.element)
                    }
                    l.x1 = b;
                    l.y1 = d;
                    l.x2 = e;
                    l.y2 = f;
                    h = this.headers[b] ? this.headers[b].offsetLeft : 0;
                    var n = this.headers[e] ? this.headers[e].offsetLeft : 0,
                        q = this.rows[d].element ?
                        this.rows[d].element.offsetTop : 0,
                        p = this.rows[f].element ? this.rows[f].element.offsetTop : 0;
                    l.element.style.top = _$_43fc[141];
                    l.element.style.left = _$_43fc[141];
                    l.active = 0;
                    var r = this.options.freezeColumns || 0,
                        w = !1,
                        m = null,
                        v = null,
                        u = null,
                        x = null;
                    this.tbody.firstChild && (v = parseInt(this.tbody.firstChild.getAttribute(_$_43fc[43])), x = parseInt(this.tbody.lastChild.getAttribute(_$_43fc[43])));
                    this.thead.lastChild && (m = parseInt(this.thead.lastChild.children[r + 1].getAttribute(_$_43fc[42])), u = parseInt(this.thead.lastChild.lastChild.getAttribute(_$_43fc[42])),
                        this.thead.lastChild.children[1].style.display == _$_43fc[40] && (m = Ia.call(this, m, v)), this.thead.lastChild.lastChild.style.display == _$_43fc[40] && (u = Ja.call(this, u, x)));
                    null == m || e < m && b >= r || b > u || f < v || d > x || (h ? l.element.style.borderLeftColor = l.color ? l.color : _$_43fc[3] : (h = this.thead.lastChild.children[r + 1].offsetLeft, l.element.style.borderLeftColor = g == _$_43fc[127] ? _$_43fc[142] : _$_43fc[143]), n ? (n += this.headers[e].offsetWidth, l.element.style.borderRightColor = l.color ? l.color : _$_43fc[3]) : (e > u ? (n = this.headers[u].offsetLeft +
                        this.headers[u].offsetWidth, w = !0) : n = this.thead.lastChild.children[r].offsetLeft + this.thead.lastChild.children[r].offsetWidth, l.element.style.borderRightColor = g == _$_43fc[127] ? _$_43fc[142] : _$_43fc[143]), n = n - h - 1, q ? l.element.style.borderTopColor = l.color ? l.color : _$_43fc[3] : (q = this.tbody.firstChild.offsetTop, l.element.style.borderTopColor = g == _$_43fc[127] ? _$_43fc[142] : _$_43fc[143]), p ? (p += this.rows[f].element.offsetHeight, l.element.style.borderBottomColor = l.color ? l.color : _$_43fc[3]) : (p = this.rows[x].element.offsetTop +
                        this.rows[x].element.offsetHeight, l.element.style.borderBottomColor = g == _$_43fc[127] ? _$_43fc[142] : _$_43fc[143], w = !0), p = p - q - 1, this.options.freezeColumns && (l.element.style.borderLeftWidth = _$_43fc[3], l.element.style.borderRightWidth = _$_43fc[3], d = 0 < this.content.scrollLeft ? ia.getWidth.call(this) : 0) && (b = b < this.options.freezeColumns, e = e < this.options.freezeColumns, b && e || !b && e || (b && !e ? (e = d + this.content.scrollLeft, e > h + n && (l.element.style.borderRightWidth = _$_43fc[144], n += e - (h + n))) : (e = d + this.content.scrollLeft,
                        e > h && (e -= h, e > n ? h = q = -2E3 : (h = h + e + 1, n -= e, l.element.style.borderLeftWidth = _$_43fc[144]))))), l.element.style.top = q + _$_43fc[44], l.element.style.left = h + _$_43fc[44], l.element.style.width = n + _$_43fc[44], l.element.style.height = p + _$_43fc[44], l.t = q, l.l = h, l.w = n, l.h = p, l.active = 1);
                    g == _$_43fc[126] && (l.active && 1 != w ? (this.corner.style.top = q + p - 2 + _$_43fc[44], this.corner.style.left = h + n - 2 + _$_43fc[44]) : (this.corner.style.top = _$_43fc[141], this.corner.style.left = _$_43fc[141]));
                    this.borders[g] = l
                },
                c = function(b, d) {
                    b.element.style.top =
                        _$_43fc[141];
                    b.element.style.left = _$_43fc[141];
                    b.active = 0;
                    d && (b.x1 = null, b.y1 = null, b.x2 = null, b.y2 = null)
                };
            k[_$_43fc[11]] = function(b, d, e, f, g, h) {
                a.call(this, b, d, e, f, g, h);
                g == _$_43fc[126] && (Aa.call(this, !0), G.call(this, _$_43fc[104], {
                    x1: b,
                    y1: d,
                    x2: e,
                    y2: f
                }))
            };
            k.reset = function(b, d) {
                b && b != _$_43fc[126] || (this.corner.style.top = _$_43fc[141], this.corner.style.left = _$_43fc[141], Aa.call(this, !1));
                if (b) this.borders[b] && c(this.borders[b], d);
                else {
                    d = Object.keys(this.borders);
                    for (var e = 0; e < d.length; e++) c(this.borders[d[e]])
                }
                b &&
                    b != _$_43fc[126] || G.call(this, _$_43fc[105])
            };
            k.refresh = function(b) {
                var d = this.borders;
                if (b) d[b] && null != d[b].x1 && a.call(this, d[b].x1, d[b].y1, d[b].x2, d[b].y2, b);
                else {
                    b = Object.keys(d);
                    for (var e = 0; e < b.length; e++) d[b[e]] && null != d[b[e]].x1 && a.call(this, d[b[e]].x1, d[b[e]].y1, d[b[e]].x2, d[b[e]].y2, b[e])
                }
                this.edition && (d = this.parent.input, Z.position.call(this, d.x, d.y))
            };
            k.destroy = function(b) {
                var d;
                if (d = this.borders[b]) d.element.remove(), delete this.borders[b]
            };
            return k
        }(),
        Qa = function() {
            var k = function(a) {
                a.setGroup =
                    k[_$_43fc[11]];
                a.unsetGroup = k.unset
            };
            k[_$_43fc[11]] = function(a, c, b) {
                0 == a && (b = document.createElement(_$_43fc[145]), b.classList.add(_$_43fc[146]), this.thead.insertBefore(b, this.thead.firstChild), a = document.createElement(_$_43fc[147]), a.innerHTML = _$_43fc[148], b.appendChild(a), a = document.createElement(_$_43fc[147]), b.appendChild(a), c = this.headers[c]) && (c = c.querySelector(_$_43fc[149]), c || (c = document.createElement(_$_43fc[20]), c.classList.add(_$_43fc[150]), c.innerText = _$_43fc[151], a.appendChild(c)))
            };
            k.unset =
                function(a, c) {};
            return k
        }(),
        ta = function() {
            var k = {
                start: function(a) {
                    if (!N.call(this.parent, this)) return !1;
                    if (null !== a.target.getAttribute(_$_43fc[43])) {
                        var c = a.target.getAttribute(_$_43fc[43]);
                        k.event = {
                            y: parseInt(c),
                            h: a.target.offsetHeight,
                            p: a.pageY
                        }
                    } else null !== a.target.getAttribute(_$_43fc[42]) && (c = a.target.getAttribute(_$_43fc[42]), k.event = {
                        x: parseInt(c),
                        w: a.target.offsetWidth,
                        p: a.pageX
                    })
                },
                end: function(a) {
                    var c = [];
                    if (null != k.event.y) {
                        var b = parseInt(this.rows[k.event.y].element.offsetHeight);
                        var d =
                            this.getSelectedRows(!0);
                        a = d.indexOf(parseInt(k.event.y));
                        if (d.length && -1 < a)
                            for (var e = 0; e < d.length; e++) c.push(parseInt(this.rows[d[e]].element.offsetHeight));
                        else d = []; - 1 == a ? (d.push(k.event.y), c.push(k.event.h)) : c[a] = k.event.h;
                        this.setHeight(d, b, c)
                    } else if (null != k.event.x) {
                        b = parseInt(this.colgroup[k.event.x].getAttribute(_$_43fc[152]));
                        d = this.getSelectedColumns();
                        a = d.indexOf(parseInt(k.event.x));
                        if (d.length && -1 < a)
                            for (e = 0; e < d.length; e++) c.push(parseInt(this.options.columns[d[e]].width));
                        else d = []; -
                        1 == a ? (d.push(k.event.x), c.push(k.event.w)) : c[a] = k.event.w;
                        this.setWidth(d, b, c)
                    }
                    k.event = null
                },
                update: function(a) {
                    null != k.event.y ? (a = a.pageY - k.event.p, 0 < k.event.h + a && (this.rows[k.event.y].element.style.height = k.event.h + a + _$_43fc[44], this.refreshBorders())) : null != k.event.x && (a = a.pageX - k.event.p, 0 < k.event.w + a && (this.colgroup[k.event.x].setAttribute(_$_43fc[152], k.event.w + a), this.options.columns[k.event.x].width = k.event.w + a, 0 < this.options.freezeColumns && ia.width.call(this), this.refreshBorders()))
                },
                cancel: function() {
                    k.end.call(this)
                }
            };
            return k
        }(),
        La = function() {
            var k = {
                start: function(a) {
                    var c = this.parent.helper;
                    a = parseInt(a.target.getAttribute(_$_43fc[42]));
                    if (this.merged.cols[a]) console.error(_$_43fc[153]);
                    else {
                        k.event = {
                            x: a,
                            d: a
                        };
                        this.headers[a].classList.add(_$_43fc[154]);
                        for (var b = 0; b < this.tbody.children.length; b++) {
                            var d = this.tbody.children[b].getAttribute(_$_43fc[43]);
                            this.records[d][a].element.classList.add(_$_43fc[154])
                        }
                        b = this.parent.element.getBoundingClientRect();
                        a = this.headers[a].getBoundingClientRect();
                        d = this.content.getBoundingClientRect();
                        c.style.display = _$_43fc[16];
                        c.style.top = d.top - b.top + _$_43fc[44];
                        c.style.left = a.left - d.left + _$_43fc[44];
                        c.style.width = _$_43fc[155];
                        c.style.height = d.height + _$_43fc[44];
                        c.classList.add(_$_43fc[156])
                    }
                },
                end: function() {
                    this.headers[k.event.x].classList.remove(_$_43fc[154]);
                    for (var a = 0; a < this.tbody.children.length; a++) {
                        var c = this.tbody.children[a].getAttribute(_$_43fc[43]);
                        this.records[c][k.event.x].element.classList.remove(_$_43fc[154])
                    }
                    null != k.event.d && k.event.x != k.event.d && (this.moveColumn(k.event.x,
                        k.event.d), this.refreshBorders());
                    this.parent.helper.classList.remove(_$_43fc[156]);
                    this.parent.helper.style.display = _$_43fc[3];
                    k.event = null
                },
                cancel: function() {
                    k.event.d = null;
                    k.end.call(this)
                },
                update: function(a) {
                    var c = a.target.getAttribute(_$_43fc[42]);
                    if (null != c)
                        if (c = parseInt(c), this.merged.cols[c]) console.error(_$_43fc[153]);
                        else {
                            a = a.target.clientWidth / 2 > a.offsetX;
                            k.event.d = a ? k.event.x < c ? parseInt(c) - 1 : parseInt(c) : k.event.x > c ? parseInt(c) + 1 : parseInt(c);
                            c = this.headers[c].getBoundingClientRect();
                            var b =
                                this.content.getBoundingClientRect();
                            this.parent.helper.style.left = (a ? c.left : c.right) - b.left + _$_43fc[44]
                        }
                }
            };
            return k
        }(),
        na = function() {
            var k = {
                start: function(a) {
                    if (N.call(this.parent, this)) {
                        this.resetSelection();
                        var c = this.parent.helper;
                        if (null !== a.target.getAttribute(_$_43fc[43])) {
                            var b = parseInt(a.target.getAttribute(_$_43fc[43]));
                            this.merged.rows[b] ? console.error(_$_43fc[157]) : this.results ? console.error(_$_43fc[158]) : (k.event = {
                                    y: b,
                                    d: b
                                }, this.rows[b].element.classList.add(_$_43fc[154]), a = this.parent.element.getBoundingClientRect(),
                                b = this.rows[b].element.getBoundingClientRect(), c.style.display = _$_43fc[16], c.style.top = b.top - a.top + _$_43fc[44], c.style.left = _$_43fc[159], c.style.width = this.content.offsetWidth - 50 + _$_43fc[44], c.style.height = _$_43fc[155], c.classList.add(_$_43fc[160]))
                        } else if (null !== a.target.getAttribute(_$_43fc[42]))
                            if (b = parseInt(a.target.getAttribute(_$_43fc[42])), this.merged.cols[b]) console.error(_$_43fc[153]);
                            else {
                                k.event = {
                                    x: b,
                                    d: b
                                };
                                this.headers[b].classList.add(_$_43fc[154]);
                                for (a = 0; a < this.tbody.children.length; a++) {
                                    var d =
                                        this.tbody.children[a].getAttribute(_$_43fc[43]);
                                    this.records[d][b].element.classList.add(_$_43fc[154])
                                }
                                a = this.parent.element.getBoundingClientRect();
                                b = this.headers[b].getBoundingClientRect();
                                d = this.content.getBoundingClientRect();
                                c.style.display = _$_43fc[16];
                                c.style.top = d.top - a.top + _$_43fc[44];
                                c.style.left = b.left - d.left + _$_43fc[44];
                                c.style.width = _$_43fc[155];
                                c.style.height = d.height + _$_43fc[44];
                                c.classList.add(_$_43fc[156])
                            }
                    } else return !1
                },
                end: function() {
                    if (null != k.event.y) this.rows[k.event.y].element.classList.remove(_$_43fc[154]),
                        null != k.event.d && k.event.y != k.event.d && (this.moveRow(k.event.y, k.event.d), this.updateSelectionFromCoords(0, k.event.d, this.options.columns.length - 1, k.event.d)), this.parent.helper.classList.remove(_$_43fc[160]);
                    else if (null != k.event.x) {
                        this.headers[k.event.x].classList.remove(_$_43fc[154]);
                        for (var a = 0; a < this.tbody.children.length; a++) {
                            var c = this.tbody.children[a].getAttribute(_$_43fc[43]);
                            this.records[c][k.event.x].element.classList.remove(_$_43fc[154])
                        }
                        null != k.event.d && k.event.x != k.event.d && (this.moveColumn(k.event.x,
                            k.event.d), this.refreshBorders());
                        this.parent.helper.classList.remove(_$_43fc[156])
                    }
                    this.parent.helper.style.display = _$_43fc[3];
                    k.event = null
                },
                cancel: function() {
                    k.event.d = null;
                    k.end.call(this)
                },
                update: function(a) {
                    if (null != k.event.y) {
                        var c = a.target.getAttribute(_$_43fc[43]);
                        if (null != c) {
                            var b = c = parseInt(c),
                                d = a.target.clientHeight / 2 > a.offsetY;
                            d ? k.event.y < c && (b = c - 1) : k.event.y > c && (b = c + 1);
                            a = !1;
                            this.merged.rows[b] && (k.event.y > b ? this.merged.rows[b - 1] && (a = !0) : this.merged.rows[b + 1] && (a = !0));
                            a || (k.event.d = b,
                                b = this.parent.element.getBoundingClientRect(), c = this.rows[c].element.getBoundingClientRect(), this.parent.helper.style.top = (d ? c.top : c.bottom) - b.top + _$_43fc[44])
                        }
                    } else d = a.target.getAttribute(_$_43fc[42]), null != d && (b = d = parseInt(d), (c = a.target.clientWidth / 2 > a.offsetX) ? k.event.x < d && (b = d - 1) : k.event.x > d && (b = d + 1), a = !1, this.merged.cols[b] && (k.event.x > b ? this.merged.cols[b - 1] && (a = !0) : this.merged.cols[b + 1] && (a = !0)), a || (k.event.d = b, b = this.headers[d].getBoundingClientRect(), d = this.content.getBoundingClientRect(),
                        this.parent.helper.style.left = (c ? b.left : b.right) - d.left + _$_43fc[44]))
                }
            };
            return k
        }(),
        X = function() {
            var k = function(a) {
                a.getMerge = k[_$_43fc[51]];
                a.setMerge = k[_$_43fc[11]];
                a.updateMerge = k.update;
                a.removeMerge = k.remove;
                a.destroyMerged = a.destroyMerge = k.destroy
            };
            k.getHeight = function(a, c) {
                var b, d = 1;
                if (this.merged.rows[a])
                    for (var e = 0; e < this.options.columns.length; e++) {
                        var f = A.getColumnNameFromCoords(e, a);
                        if (f = this.merged.cells[f])
                            if (b = this.options.mergeCells[f]) c && void 0 !== c[f] || (b[1] > d && (d = b[1]), c && (c[f] =
                                this.records[a][e].element && !this.records[a][e].merged ? !0 : !1))
                    }
                return d - 1
            };
            k.getWidth = function(a, c) {
                var b, d = 1;
                if (this.merged.cols[a])
                    for (var e = 0; e < this.options.data.length; e++) {
                        var f = A.getColumnNameFromCoords(a, e);
                        if (f = this.merged.cells[f])
                            if (b = this.options.mergeCells[f]) c && void 0 !== c[f] || (b[0] > d && (d = b[0]), c && (c[f] = this.records[e][a].element && !this.records[e][a].merged ? !0 : !1))
                    }
                return d - 1
            };
            k[_$_43fc[51]] = function(a) {
                return a ? this.options.mergeCells[a] : this.options.mergeCells
            };
            k[_$_43fc[11]] = function(a,
                c, b) {
                if (!N.call(this.parent, this)) return !1;
                this.options.mergeCells || (this.options.mergeCells = {});
                var d, e = null,
                    f = {},
                    g = {},
                    h = {},
                    l = !1;
                typeof a == _$_43fc[17] ? h[a] = [c, b] : typeof a == _$_43fc[84] ? h = a : ((e = this.getHighlighted()) ? e = [A.getColumnNameFromCoords(e[0], e[1]), e[2] - e[0] + 1, e[3] - e[1] + 1] : (jSuites.notification({
                    message: z(_$_43fc[161])
                }), e = !1), e && (h[e[0]] = [e[1], e[2]]));
                c = Object.keys(h);
                if (c.length) {
                    for (b = 0; b < c.length; b++) {
                        var n = c[b],
                            q = h[c[b]][0],
                            p = h[c[b]][1];
                        q = parseInt(q);
                        p = parseInt(p);
                        if ((!q || 2 > q) && (!p || 2 >
                                p)) console.log(z(_$_43fc[162]) + _$_43fc[136] + n);
                        else if (this.options.mergeCells[n]) g[n] = [q, p], f[n] = this.options.mergeCells[n], k.update.call(this, n, q, p);
                        else {
                            e = A.getCoordsFromColumnName(n);
                            for (var r = e[1]; r < e[1] + p; r++)
                                for (var w = e[0]; w < e[0] + q; w++) a = A.getColumnNameFromCoords(w, r), (d = this.merged.cells[a]) && this.options.mergeCells[d] && (f[d] = this.options.mergeCells[d], k.applyDestroy.call(this, d));
                            g[n] = this.options.mergeCells[n] = [q, p];
                            k.applyCreate.call(this, n);
                            I.attached.call(this, e[0], e[1]) || (l = e)
                        }
                    }
                    Object.keys(g).length &&
                        (l && C[_$_43fc[46]].call(this, e[1], e[0]), Q.call(this.parent, {
                            worksheet: this,
                            action: _$_43fc[69],
                            newValue: g,
                            oldValue: f
                        }), G.call(this, _$_43fc[69], [g]), k.build.call(this), ua.refresh.call(this), da.refresh.call(this), B.call(this.parent, _$_43fc[163], this, g, f))
                }
            };
            k.update = function(a, c, b) {
                var d = this.options.mergeCells;
                if (d[a]) {
                    var e = d[a][1];
                    if (c !== d[a][0] || e !== b) k.applyDestroy.call(this, a), c && b && (d[a] = [c, b], k.applyCreate.call(this, a))
                }
            };
            k.remove = function(a, c) {
                if (!N.call(this.parent, this)) return !1;
                c = {};
                var b = {},
                    d = {};
                typeof a == _$_43fc[17] ? d[a] = !0 : typeof a == _$_43fc[84] && (d = a);
                a = Object.keys(d);
                for (d = 0; d < a.length; d++) this.options.mergeCells[a[d]] && (c[a[d]] = this.options.mergeCells[a[d]], b[a[d]] = !0, k.applyDestroy.call(this, a[d], !0));
                Q.call(this.parent, {
                    worksheet: this,
                    action: _$_43fc[70],
                    newValue: b,
                    oldValue: c
                });
                G.call(this, _$_43fc[70], {
                    data: b
                })
            };
            k.destroy = function() {
                k.remove.call(this, this.options.mergeCells, !0)
            };
            k.applyCreate = function(a, c) {
                var b = this.options.mergeCells;
                if (b[a]) {
                    var d = b[a][0] || 1,
                        e = b[a][1] || 1;
                    a = A.getCoordsFromColumnName(a);
                    if (!this.records[a[1]][a[0]].merged || 1 == c)
                        if (b = I[_$_43fc[51]].call(this, a[0], a[1])) {
                            b.style.display = _$_43fc[3];
                            1 < d ? b.setAttribute(_$_43fc[52], d) : b.removeAttribute(_$_43fc[52]);
                            1 < e ? b.setAttribute(_$_43fc[164], e) : b.removeAttribute(_$_43fc[164]);
                            1 == this.options.textOverflow && (b.style.overflow = _$_43fc[49]);
                            for (var f = 0; f < e; f++)
                                for (var g = 0; g < d; g++) {
                                    c = a[0] + g;
                                    var h = a[1] + f;
                                    if (b = this.records[h][c])
                                        if (b.merged = [g, f, b.v, b.v], 0 != g || 0 != f) b.element || I[_$_43fc[51]].call(this, c, h), b.element.style.display =
                                            _$_43fc[40], b.merged[3] = b.element.innerText, b.element.innerText = _$_43fc[3], K.call(this, c, h, _$_43fc[3])
                                }
                        }
                }
            };
            k.applyDestroy = function(a, c) {
                var b = this.options.mergeCells;
                if (b[a]) {
                    var d = A.getCoordsFromColumnName(a),
                        e = d[0];
                    d = d[1];
                    var f = b[a][0],
                        g = b[a][1];
                    delete this.options.mergeCells[a];
                    for (a = 0; a < g; a++)
                        for (var h = 0; h < f; h++)
                            if (b = this.records[d + a][e + h]) b.element && (0 == h && 0 == a ? (b.element.removeAttribute(_$_43fc[52]), b.element.removeAttribute(_$_43fc[164])) : (b.element.style.display = _$_43fc[3], 1 == c && (K.call(this,
                                e + h, d + a, b.merged[2]), b.element.innerHTML = b.merged[3]))), b.merged && delete b.merged
                }
            };
            k.updateConfig = function(a, c, b, d) {
                if (1 == c) var e = [b];
                else {
                    e = [];
                    for (var f = b; f < b + d; f++) e.push(f)
                }
                var g = this.options.mergeCells,
                    h = Object.keys(g),
                    l = {};
                for (f = 0; f < h.length; f++) {
                    b = A.getCoordsFromColumnName(h[f]);
                    for (var n = b[a]; n < b[a] + g[h[f]][a]; n++) - 1 !== e.indexOf(n) && (l[h[f]] || (l[h[f]] = [g[h[f]][0], g[h[f]][1]], 1 == c && (l[h[f]][a] += d)), 0 == c && l[h[f]][a]--)
                }
                h = Object.keys(l);
                for (f = 0; f < h.length; f++) k.update.call(this, h[f], l[h[f]][0],
                    l[h[f]][1]);
                k.build.call(this)
            };
            k.build = function() {
                var a = this.options.mergeCells;
                this.merged = {
                    cols: [],
                    rows: [],
                    cells: []
                };
                if (a)
                    for (var c = Object.keys(a), b = 0; b < c.length; b++) {
                        for (var d = A.getCoordsFromColumnName(c[b]), e = parseInt(a[c[b]][0]), f = parseInt(a[c[b]][1]), g = 0; g < e; g++) this.merged.cols[d[0] + g] = !0;
                        for (var h = 0; h < f; h++) this.merged.rows[d[1] + h] = !0;
                        for (h = 0; h < f; h++)
                            for (g = 0; g < e; g++) {
                                var l = A.getColumnNameFromCoords(d[0] + g, d[1] + h);
                                this.merged.cells[l] = c[b]
                            }
                    }
            };
            k.batch = function(a) {
                var c = Object.keys(a);
                if (c.length)
                    for (var b =
                            0; b < c.length; b++) !1 === a[c[b]] && k.applyCreate.call(this, c[b])
            };
            return k
        }(),
        ya = function() {
            var k = function(a) {
                a.getHeader = k[_$_43fc[51]];
                a.setHeader = k[_$_43fc[11]];
                a.getHeaders = k.all
            };
            k[_$_43fc[51]] = function(a) {
                var c;
                if (c = this.options.columns) return c[a].title || A.getColumnName(a)
            };
            k.all = function(a) {
                for (var c = [], b = 0; b < this.options.columns.length; b++) c.push(k[_$_43fc[51]].call(this, b));
                return a ? c : c.join(this.options.csvDelimiter)
            };
            k[_$_43fc[11]] = function(a, c) {
                if (!N.call(this.parent, this)) return !1;
                var b;
                if (b = this.options.columns[a]) {
                    var d = k[_$_43fc[51]].call(this, a);
                    if (!c && (c = prompt(z(_$_43fc[165]), d), !c)) return !1;
                    b.title = c;
                    if (b = this.headers[a]) b.innerHTML = c, b.setAttribute(_$_43fc[166], c);
                    Q.call(this.parent, {
                        worksheet: this,
                        action: _$_43fc[75],
                        column: a,
                        oldValue: d,
                        newValue: c
                    });
                    G.call(this, _$_43fc[75], {
                        column: a,
                        title: c
                    });
                    B.call(this.parent, _$_43fc[167], this, a, c, d)
                }
            };
            k.create = function(a) {
                var c = this.options.columns[a],
                    b = c.width || this.options.defaultColWidth,
                    d = c.align || _$_43fc[53],
                    e = c.title || A.getColumnName(a),
                    f = document.createElement(_$_43fc[147]);
                this.parent.config.stripHTML ? f.innerText = e : f.innerHTML = e;
                f.setAttribute(_$_43fc[42], a);
                f.style.textAlign = d;
                c.title && f.setAttribute(_$_43fc[166], f.innerText);
                c.tooltip && (f.title = c.tooltip);
                d = this.options.filters;
                !0 === c.filter && (d = !0);
                !1 === c.filter && (d = !1);
                !0 === d && f.classList.add(_$_43fc[168]);
                !1 === c.visible && (b = _$_43fc[97]);
                d = document.createElement(_$_43fc[169]);
                d.setAttribute(_$_43fc[152], b);
                c.type == _$_43fc[49] && (f.style.display = _$_43fc[40], d.style.display = _$_43fc[40]);
                this.headers[a] = f;
                this.colgroup[a] = d
            };
            return k
        }(),
        W = function() {
            var k = function(c, b, d) {
                    var e = this.footers.content,
                        f;
                    e && e[b] && e[b][c] && (f = e[b][c].element) && (void 0 === d && (e = this.options.footers, d = e[b] && e[b][c] ? e[b][c] : _$_43fc[3]), M(d) && 1 == this.parent.config.parseFormulas && (d = S.execute.call(this, d, c, null, !1)), this.options.applyMaskOnFooters && (e = Z[_$_43fc[51]].call(this, c), d = t.editors.general.parseValue(c, b, d, this, e[1], f)), d instanceof Element || d instanceof HTMLDocument ? (f.innerHTML = _$_43fc[3], f.appendChild(d)) :
                        1 == this.parent.config.stripHTML ? f.innerText = d : f.innerHTML = d)
                },
                a = function(c) {
                    c.getFooter = a[_$_43fc[51]];
                    c.setFooter = a[_$_43fc[11]];
                    c.resetFooter = a.reset;
                    c.refreshFooter = a.refresh;
                    c.getFooterValue = a.value;
                    c.setFooterValue = a.value
                };
            a[_$_43fc[51]] = function() {
                return this.options.footers
            };
            a[_$_43fc[11]] = function(c) {
                if (c) {
                    var b = this.options.footers;
                    this.options.footers = c
                }
                this.options.footers != b && (W.build.call(this), C.limited.call(this) ? C.refresh.call(this) : a.render.call(this), G.call(this, _$_43fc[170], {
                        data: this.options.footers
                    }),
                    B.call(this.parent, _$_43fc[171], this, this.options.footers, b))
            };
            a.refresh = function() {
                var c = this.options.footers;
                if (c && c.length)
                    for (var b = 0; b < c.length; b++)
                        for (var d = 0; d < c[b].length; d++) k.call(this, d, b, c[b][d])
            };
            a.value = function(c, b, d) {
                if (void 0 === d) return this.options.footers[b][c];
                k.call(this, c, b, d);
                G.call(this, _$_43fc[172], {
                    x: c,
                    y: b,
                    value: d
                });
                B.call(this.parent, _$_43fc[173], this, c, b, d)
            };
            a.reset = function() {
                var c = this.options.footers;
                this.options.footers = null;
                this.footers.content = null;
                this.tfoot.innerHTML =
                    _$_43fc[3];
                G.call(this, _$_43fc[174], {});
                B.call(this.parent, _$_43fc[171], this, null, c)
            };
            a.create = function(c, b) {
                var d = this.footers.content[b][c];
                if (d && d.element) return d.element;
                d = this.footers.content[b][c] = {};
                var e = this.options.columns[c].align ? this.options.columns[c].align : _$_43fc[53];
                d.element = document.createElement(_$_43fc[147]);
                d.element.style.textAlign = e;
                d.element.setAttribute(_$_43fc[42], c);
                d.element.setAttribute(_$_43fc[43], b);
                k.call(this, c, b);
                this.options.columns[c].type == _$_43fc[49] && (d.element.style.display =
                    _$_43fc[40]);
                return d.element
            };
            a.build = function() {
                this.footers = {
                    element: this.tfoot,
                    content: []
                };
                if (this.options.footers)
                    for (var c = 0; c < this.options.footers.length; c++) {
                        if (!this.footers.content[c]) {
                            this.footers.content[c] = [];
                            var b = document.createElement(_$_43fc[145]),
                                d = document.createElement(_$_43fc[147]);
                            d.innerHTML = _$_43fc[148];
                            b.appendChild(d);
                            this.footers.element.appendChild(b)
                        }
                        for (b = 0; b < this.options.columns.length; b++) this.options.footers[c][b] || (this.options.footers[c][b] = _$_43fc[3]), this.footers.content[c][b] ||
                            (this.footers.content[c][b] = {})
                    }
            };
            a.render = function() {
                for (var c = this.options.footers, b = 0; b < c.length; b++)
                    for (var d = 0; d < c[b].length; d++) {
                        var e = W.create.call(this, d, b);
                        this.tfoot.children[b].appendChild(e)
                    }
            };
            a.adjust = function(c, b, d) {
                var e = [],
                    f = this.options.footers,
                    g = this.footers.content;
                if (f)
                    if (1 == d) {
                        if (this.headers[c] && this.headers[c].parentNode) var h = this.headers[c] == this.thead.lastChild.lastChild ? !0 : !1,
                            l = !0;
                        for (d = 0; d < f.length; d++) {
                            for (var n = f[d].splice(c), q = c; q < c + b; q++) f[d][q] = _$_43fc[3];
                            f[d] = f[d].concat(n);
                            n = g[d].splice(c);
                            for (q = c; q < c + b; q++) g[d][q] = {
                                element: a.create.call(this, q, d)
                            }, l && (1 == h ? this.tfoot.children[d].appendChild(g[d][q].element) : this.tfoot.children[d].insertBefore(g[d][q].element, this.tfoot.children[d].children[c].nextSibling));
                            g[d] = g[d].concat(n)
                        }
                    } else
                        for (d = 0; d < f.length; d++) {
                            for (q = c; q < c + b; q++)(h = this.footers.content[d][q].element) && h.parentNode && h.parentNode.removeChild(h);
                            g[d].splice(c, b);
                            e[d] = f[d].splice(c, b)
                        }
                return e
            };
            return a
        }(),
        la = function() {
            var k = function(a) {
                a.setNestedCell = k.cell;
                a.getNestedCell = k.cell;
                a.setNestedHeaders = k[_$_43fc[11]];
                a.getNestedHeaders = k[_$_43fc[51]];
                a.resetNestedHeaders = k.reset
            };
            k[_$_43fc[51]] = function() {
                return this.options.nestedHeaders
            };
            k[_$_43fc[11]] = function(a) {
                k.reset.call(this);
                this.options.nestedHeaders = a;
                k.build.call(this);
                k.render.call(this);
                da.refresh.call(this);
                P.call(this);
                G.call(this, _$_43fc[175], {
                    data: a
                });
                B.call(this.parent, _$_43fc[176], this, a)
            };
            k.reset = function() {
                var a = [],
                    c = this.thead.children;
                this.options.nestedHeaders = null;
                for (var b = 0; b <
                    c.length; b++) c[b].classList.contains(_$_43fc[177]) && a.push(c[b]);
                if (a.length)
                    for (; c = a.shift();) c.remove();
                this.nested = null;
                da.refresh.call(this);
                P.call(this);
                G.call(this, _$_43fc[178]);
                B.call(this.parent, _$_43fc[176], this, [])
            };
            k.cell = function(a, c, b) {
                if (b) {
                    if (!N.call(this.parent, this)) return !1;
                    var d = this.options.nestedHeaders[c][a],
                        e = this.nested.content[c][a].element;
                    void 0 !== b.title && (e.innerText = b.title);
                    void 0 !== b.tooltip && (e.title = b.tooltip);
                    void 0 !== b.colspan && (d.colspan = b.colspan, e.setAttribute(_$_43fc[52],
                        b.colspan), e.style.display = 0 == d.colspan ? _$_43fc[40] : _$_43fc[3]);
                    G.call(this, _$_43fc[179], {
                        x: a,
                        y: c,
                        properties: b
                    });
                    B.call(this.parent, _$_43fc[180], this, a, c, b)
                } else return this.nested.content[c][a].element
            };
            k.range = function(a) {
                a = this.options.nestedHeaders[a];
                for (var c = [], b = 0, d = 0; d < a.length; d++) {
                    var e = parseInt(a[d].colspan);
                    c[d] = [b, b + e - 1];
                    b += e
                }
                return c
            };
            k.getColumns = function(a) {
                a = this.options.nestedHeaders[a];
                for (var c = [], b = 0; b < a.length; b++)
                    for (var d = a[b].colspan, e = 0; e < d; e++) c.push(b);
                return c
            };
            k.create =
                function(a, c) {
                    if (this.nested.content[c][a]) return this.nested.content[c][a].element;
                    var b = this.options.nestedHeaders[c][a] || {},
                        d = this.nested.content[c][a] = {};
                    b.colspan || (b.colspan = 1);
                    b.align || (b.align = _$_43fc[53]);
                    b.title || (b.title = _$_43fc[3]);
                    var e = document.createElement(_$_43fc[147]);
                    e.setAttribute(_$_43fc[181], a);
                    e.setAttribute(_$_43fc[182], c);
                    e.setAttribute(_$_43fc[52], b.colspan);
                    e.setAttribute(_$_43fc[183], b.align);
                    e.innerHTML = b.title;
                    b.tooltip && (e.title = b.tooltip);
                    return d.element = e
                };
            k.build =
                function() {
                    this.nested || (this.nested = {
                        content: []
                    });
                    if (this.options.nestedHeaders)
                        for (var a = 0; a < this.options.nestedHeaders.length; a++) {
                            this.nested.content[a] = [];
                            var c = document.createElement(_$_43fc[147]);
                            c.innerHTML = _$_43fc[148];
                            var b = document.createElement(_$_43fc[145]);
                            b.classList.add(_$_43fc[177]);
                            b.appendChild(c);
                            this.thead.insertBefore(b, this.thead.lastChild)
                        }
                };
            k.render = function() {
                for (var a, c = this.options.nestedHeaders, b = 0; b < c.length; b++)
                    for (var d = 0; d < this.thead.lastChild.children.length; d++) {
                        a =
                            this.thead.lastChild.children[d].getAttribute(_$_43fc[42]);
                        var e = k.getColumns.call(this, b);
                        void 0 !== e[a] && k.renderCell.call(this, e[a], b)
                    }
            };
            k.renderCell = function(a, c, b) {
                var d = la.create.call(this, a, c);
                d.parentNode ? (a = d.getAttribute(_$_43fc[52]), a++, d.setAttribute(_$_43fc[52], a)) : (d.setAttribute(_$_43fc[52], 1), b ? this.thead.children[c].insertBefore(d, this.thead.children[c].children[b]) : this.thead.children[c].appendChild(d), 0 == a && 0 < this.options.freezeColumns && (d.classList.add(_$_43fc[50]), d.style.left =
                    va.call(this) ? _$_43fc[159] : _$_43fc[155]))
            };
            k.adjust = function(a, c, b, d) {
                var e = this.options.nestedHeaders;
                if (e)
                    if (1 == b)
                        if (d && d.nested)
                            for (b = 0; b < e.length; b++)
                                for (var f = 0; f < e[b].length; f++) k.cell.call(this, f, b, {
                                    colspan: d.nested[b][f]
                                });
                        else
                            for (b = 0; b < e.length; b++) {
                                if (f = k.getColumns.call(this, b)) {
                                    f = f[a];
                                    var g = parseInt(e[b][f].colspan) + c;
                                    k.cell.call(this, f, b, {
                                        colspan: g
                                    })
                                }
                            } else {
                                d = [];
                                for (b = 0; b < e.length; b++)
                                    if (g = k.getColumns.call(this, b)) {
                                        d[b] = [];
                                        for (f = 0; f < c; f++) {
                                            var h = g[a + f];
                                            typeof d[b][h] == _$_43fc[13] && (d[b][h] =
                                                e[b][h].colspan);
                                            e[b][h].colspan--
                                        }
                                        g = Object.keys(d[b]);
                                        for (f = 0; f < g.length; f++) k.cell.call(this, g[f], b, {
                                            colspan: e[b][g[f]].colspan
                                        })
                                    } return d
                            }
            };
            return k
        }(),
        ha = function() {
            var k = function(a, c) {
                var b = function(q) {
                        var p;
                        (p = this.results) ? q = p.indexOf(q): p = this.rows;
                        q--;
                        for (var r = q; 0 <= r; r--)
                            if (q = this.results ? p[r] : r, this.rows[q] && this.rows[q].element) return q;
                        return 0
                    },
                    d = function(q) {
                        var p;
                        (p = this.results) ? q = p.indexOf(q): p = this.rows;
                        q++;
                        for (var r = q; r < p.length; r++)
                            if (q = this.results ? p[r] : r, this.rows[q] && this.rows[q].element) return q;
                        return p[p.length - 1]
                    },
                    e = function(q, p) {
                        for (; 0 <= q; q--)
                            if (this.records[p][q].element && this.records[p][q].element.style.display != _$_43fc[40]) return q;
                        return 0
                    },
                    f = function(q, p) {
                        for (; q < this.options.columns.length; q++)
                            if (this.records[p][q].element && this.records[p][q].element.style.display != _$_43fc[40]) return q;
                        return this.options.columns - 1
                    },
                    g = this.getHighlighted(),
                    h = g[0],
                    l = g[1],
                    n = g[2];
                g = g[3];
                a = parseInt(a);
                c = parseInt(c);
                null != a && null != c && (0 < a - n ? (f = f.call(this, parseInt(n) + 1, c), a = parseInt(a)) : (f = parseInt(a),
                    a = e.call(this, parseInt(h) - 1, c)), 0 < c - g ? (d = d.call(this, parseInt(g)), c = parseInt(c)) : (d = parseInt(c), c = b.call(this, parseInt(l))), a - f <= c - d ? (f = parseInt(h), a = parseInt(n)) : (d = parseInt(l), c = parseInt(g)), this.setBorder(f, d, a, c, _$_43fc[138]))
            };
            k.execute = function(a) {
                if (!N.call(this.parent, this)) return !1;
                var c = this.borders.cloning;
                if (a)
                    if (c && c.active) {
                        a = c.x1;
                        var b = c.y1,
                            d = c.x2,
                            e = c.y2
                    } else return !1;
                else if (d = this.getSelected(), d.length) a = d[0].x, b = parseInt(d[d.length - 1].y) + 1, d = d[d.length - 1].x, e = this.options.data.length -
                    1;
                else return !1;
                var f, g = 0,
                    h = [],
                    l = [],
                    n = !1,
                    q = this.getHighlighted(),
                    p = Ba[_$_43fc[51]].call(this, q, !1, !1),
                    r = this.getSelected();
                if (f = this.getStyle(r, !0)) {
                    var w = Object.keys(f);
                    if (w.length)
                        for (var m = 0; m < w.length; m++) h.push(f[w[m]])
                }
                if (q[0] == a) var v = b < q[1] ? b - q[1] : 1,
                    u = 0;
                else u = a < q[0] ? a - q[0] : 1, v = 0;
                w = 0;
                a = parseInt(a);
                d = parseInt(d);
                b = parseInt(b);
                e = parseInt(e);
                for (var x = b; x <= e; x++)
                    if (!this.results || -1 != this.results.indexOf(x)) {
                        void 0 == p[g] && (g = 0);
                        f = 0;
                        q[0] != a && (u = a < q[0] ? a - q[0] : 1);
                        for (m = a; m <= d; m++) {
                            if (!(0 != n || this.records[x][m].element &&
                                    this.records[x][m].element.classList.contains(_$_43fc[18]))) {
                                if ((!c || 1 != c.active) && K.call(this, m, x)) {
                                    n = !0;
                                    continue
                                }
                                void 0 == p[g] ? f = 0 : void 0 == p[g][f] && (f = 0);
                                var y = p[g][f];
                                y && !p[1] && 1 == this.parent.config.autoIncrement && (this.options.columns[m].type == _$_43fc[56] || this.options.columns[m].type == _$_43fc[14] ? M(y) ? y = S.shiftFormula(y, u, x - q[1]) : y == Number(y) && (y = Number(y) + v) : this.options.columns[m].type == _$_43fc[184] && (y = new Date(y), y.setDate(y.getDate() + v), y = y.getFullYear() + _$_43fc[185] + jSuites.two(parseInt(y.getMonth() +
                                    1)) + _$_43fc[185] + jSuites.two(y.getDate()) + _$_43fc[132] + _$_43fc[186]));
                                y = {
                                    x: m,
                                    y: x,
                                    value: y
                                };
                                h && null != h[w] && (y.style = h[w], w++, w >= h.length && (w = 0));
                                l.push(y)
                            }
                            f++;
                            q[0] != a && u++
                        }
                        g++;
                        v++
                    } l.length && (r[0].x < a && (a = r[0].x), r[0].y < b && (b = r[0].y), r[r.length - 1].x > d && (d = r[r.length - 1].x), r[r.length - 1].y > e && (e = r[r.length - 1].y), this.setValue(l), this.resetBorders(_$_43fc[138], !0), this.updateSelectionFromCoords(a, b, d, e))
            };
            k.end = function(a) {
                ha.event = !1;
                ha.execute.call(this, !0)
            };
            k.cancel = function(a) {
                ha.event = !1;
                this.resetBorders(_$_43fc[138],
                    !0)
            };
            return k
        }(),
        Ba = function() {
            var k = function(a) {
                a.getData = k[_$_43fc[51]];
                a.getJson = k[_$_43fc[51]];
                a.setData = k[_$_43fc[11]];
                a.loadData = k.load;
                a.getRowData = k.getRowData;
                a.setRowData = k.setRowData;
                a.getColumnData = k.getColumnData;
                a.setColumnData = k.setColumnData;
                a.updateData = k.update;
                a.refresh = k.refresh;
                a.data = k[_$_43fc[51]];
                a.download = k.download;
                a.copy = k.copy;
                a.paste = k.paste;
                a.cut = function() {
                    return a.copy(!0)
                }
            };
            k[_$_43fc[51]] = function(a, c, b) {
                !0 === a && (a = this.getHighlighted());
                for (var d = a ? !0 : !1, e, f = [],
                        g = this.options.columns.length, h = this.options.data.length, l, n = this.dataType && typeof b == _$_43fc[13] ? 1 : 0, q = 0; q < h; q++) {
                    l = null;
                    e = n ? {} : [];
                    for (var p = 0; p < g; p++) {
                        var r;
                        if (!(r = !d)) {
                            r = a[1];
                            var w = a[2],
                                m = a[3];
                            r = p >= a[0] && p <= w && q >= r && q <= m ? !0 : !1
                        }
                        if (r && (!d || !this.results || -1 != this.results.indexOf(q))) {
                            r = K.call(this, p, q);
                            if (c) {
                                if (d && !1 === this.rows[q].visible) continue;
                                l = aa.processed.call(this, p, q);
                                void 0 !== l && (r = l)
                            }
                            b && r.match && (r.match(b) || r.match(/\n/) || r.match(/"/)) && (r = r.replace(new RegExp(_$_43fc[187], _$_43fc[188]),
                                _$_43fc[189]), r = _$_43fc[187] + r + _$_43fc[187]);
                            n ? jSuites.path.call(e, _$_43fc[3] + V.call(this, p), r) : e.push(r);
                            l = !0
                        }
                    }
                    l && (b ? f.push(e.join(b)) : f.push(e))
                }
                return b ? f.join(_$_43fc[10]) + _$_43fc[10] : f
            };
            k.parse = function() {
                for (var a, c = {}, b = 0, d, e = [], f = this.options.data, g = 0; g < f.length; g++) a = null, d = g, f[g].data && (Array.isArray(f[g].data) || typeof f[g].data == _$_43fc[84]) && (typeof f[g].id == _$_43fc[14] && (a = parseInt(f[g].id)), typeof f[g].row == _$_43fc[14] && (d = f[g].row), e[d] = f[g].data, f[g] = []), d > b && (b = d), a && (c[d] = a);
                if (Object.keys(e).length)
                    for (g =
                        0; g <= b; g++) e[g] && (f[g] = e[g]);
                b = this.getPrimaryKey();
                if (!1 !== b)
                    for (b = V.call(this, b), g = 0; g < f.length; g++) c[g] || !1 === b || (a = parseInt(f[g][b]), 0 < a && (c[g] = a));
                return c
            };
            k.type = function() {
                for (var a = this.dataType = 0; a < this.options.columns.length; a++) typeof this.options.columns[a].name !== _$_43fc[13] && (this.dataType = 1)
            };
            k[_$_43fc[11]] = function(a) {
                a = k.standardize(a);
                var c = {},
                    b = [],
                    d = {};
                var e = Object.keys(a[0]).length - this.options.columns.length;
                0 < e && this.insertRow(e);
                var f = a.length - this.options.data.length;
                0 < f &&
                    this.insertRow(f);
                for (var g = 0; g < this.options.data.length; g++) {
                    void 0 === a[g] && (a[g] = {
                        row: g,
                        data: []
                    });
                    for (var h = 0; h < this.options.columns.length; h++) a[g].data[h] || (a[g].data[h] = _$_43fc[3])
                }
                for (h = 0; h < this.options.columns.length; h++) {
                    var l = V.call(this, h);
                    d[l] = h
                }
                for (g = 0; g < a.length; g++) {
                    f = a[g].row;
                    l = Object.keys(a[g].data);
                    for (h = 0; h < l.length; h++) e = null, void 0 !== d[l[h]] ? e = d[l[h]] : -1 < l[h] && (e = parseInt(l[h])), null !== e && b.push({
                        x: e,
                        y: f,
                        value: a[g].data[l[h]]
                    });
                    a[g].id && (c[f] = a[g].id)
                }
                pa.setId.call(this, c);
                aa[_$_43fc[11]].call(this,
                    b, null, !0)
            };
            k.build = function() {
                this.results = null;
                this.rows = [];
                this.records = [];
                this.sequence = 0;
                k.type.call(this);
                if (this.options.data) {
                    var a = k.parse.call(this),
                        c = this.options.data.length;
                    this.options.minDimensions[1] > c && (c = this.options.minDimensions[1]);
                    for (var b = 0; b < c; b++) {
                        var d = a[b] ? a[b] : null;
                        ka.row.call(this, b, d)
                    }
                }
            };
            k.load = function(a) {
                this.resetBorders();
                this.resetSelection();
                this.tbody.innerText = _$_43fc[3];
                this.options.data = a;
                k.build.call(this);
                C.call(this)
            };
            k.refresh = function(a) {
                if (typeof a !==
                    _$_43fc[13])
                    for (var c = 0; c < this.options.columns.length; c++) {
                        var b = this.records[a] && this.records[a][c].element ? this.records[a][c].element : null,
                            d = Z[_$_43fc[51]].call(this, c, a);
                        typeof d[0].updateCell == _$_43fc[85] && d[0].updateCell(b, K.call(this, c, a), c, a, this, d[1]);
                        b && I.applyOverflow.call(this, b, c, a)
                    } else if (this.options.url) {
                        var e = this;
                        1 == this.parent.config.loadingSpin && jSuites.loading.show();
                        jSuites.ajax({
                            url: this.options.url,
                            method: _$_43fc[59],
                            dataType: _$_43fc[60],
                            success: function(f) {
                                e.setData(f);
                                jSuites.loading.hide()
                            }
                        })
                    }
            };
            k.getRowData = function(a, c) {
                if (typeof this.options.data[a] === _$_43fc[13]) return !1;
                if (c) {
                    if (this.dataType) {
                        c = {};
                        for (var b = 0; b < this.options.columns.length; b++) {
                            var d = _$_43fc[3] + V.call(this, b),
                                e = _$_43fc[3] + aa.processed.call(this, b, a, !0);
                            jSuites.path.call(c, d, e)
                        }
                    } else
                        for (c = [], b = 0; b < this.options.columns.length; b++) c.push(aa.processed.call(this, b, a, !0));
                    return c
                }
                return JSON.parse(JSON.stringify(this.options.data[a]))
            };
            k.setRowData = function(a, c, b) {
                for (var d = [], e = 0; e < this.options.columns.length; e++) typeof c[e] ===
                    _$_43fc[13] && (c[e] = _$_43fc[3]), d.push({
                        x: e,
                        y: a,
                        value: c[e]
                    });
                d.length && this.setValue(d, null, b)
            };
            k.getColumnData = function(a, c) {
                if (typeof this.options.columns[a] === _$_43fc[13]) return !1;
                for (var b = [], d = 0; d < this.options.data.length; d++) {
                    var e = c ? aa.processed.call(this, a, d, !0) : K.call(this, a, d);
                    b.push(e)
                }
                return b
            };
            k.setColumnData = function(a, c, b) {
                for (var d = [], e = 0; e < this.rows.length; e++) typeof c[e] === _$_43fc[13] && (c[e] = _$_43fc[3]), d.push({
                    x: a,
                    y: e,
                    value: c[e]
                });
                d.length && this.setValue(d, null, b)
            };
            k.copy = function(a) {
                var c =
                    this,
                    b = this.getHighlighted(),
                    d = function() {
                        c.setBorder(b[0], b[1], b[2], b[3], _$_43fc[127]);
                        c.borders.copying.clear = 1 == a ? !0 : !1
                    },
                    e = k[_$_43fc[51]].call(this, b, !0, _$_43fc[190]),
                    f = B.call(this.parent, _$_43fc[191], this, b, e);
                if (f) e = f;
                else if (!1 === f) return !1;
                navigator.clipboard ? navigator.clipboard.writeText(e).then(d) : (this.parent.textarea.value = e, this.parent.textarea.select(), document.execCommand(_$_43fc[192]), d());
                return e
            };
            k.paste = function(a, c, b) {
                if (!N.call(this.parent, this)) return !1;
                var d = this,
                    e = function() {
                        d.borders.copying &&
                            (d.resetBorders(_$_43fc[127], !0), d.borders.copying.clear = !1)
                    },
                    f = b,
                    g = null,
                    h = this.options.style,
                    l = null,
                    n = null,
                    q = [];
                if (this.borders.copying) {
                    var p = [this.borders.copying.x1, this.borders.copying.y1, this.borders.copying.x2, this.borders.copying.y2];
                    l = p[0];
                    n = p[1];
                    var r = jSuites.hash(b),
                        w = jSuites.hash(k[_$_43fc[51]].call(this, p, !0, _$_43fc[190]));
                    if (r != w) e();
                    else {
                        var m = 0,
                            v = 0;
                        b = k[_$_43fc[51]].call(this, p, !1, !1);
                        for (r = p[1]; r <= p[3]; r++)
                            if (!this.results || -1 != this.results.indexOf(r)) {
                                q[v] = [];
                                m = 0;
                                for (w = p[0]; w <= p[2]; w++) g =
                                    A.getColumnNameFromCoords(w, r), q[v][m] = h && h[g] ? h[g] : _$_43fc[193], m++;
                                v++
                            }
                    }
                }
                a = parseInt(a);
                c = parseInt(c);
                100 < f.length && !this.options.pagination && !C.limited.call(this) && console.log(_$_43fc[194]);
                if (r = B.call(this.parent, _$_43fc[195], this, b, a, c, q, f)) b = r;
                else if (!1 === r) return !1;
                if (b) {
                    Array.isArray(b) || (b = A.parseCSV(b, _$_43fc[190]));
                    if (f = this.getHighlighted()) {
                        g = [];
                        m = [];
                        v = [];
                        h = [];
                        var u = 0,
                            x = 0;
                        for (r = f[1]; r <= f[3]; r++)
                            if (!this.results || -1 != this.results.indexOf(r)) {
                                g = [];
                                m = [];
                                for (w = f[0]; w <= f[2]; w++) g.push(b[x][u]),
                                    q && q[x] && q[x][u] ? m.push(q[x][u]) : m.push(_$_43fc[3]), u++, u >= b[0].length && (u = 0);
                                v.push(g);
                                h.push(m);
                                x++;
                                x >= b.length && (x = 0)
                            } 0 == v.length % b.length && 0 == v[0].length % b[0].length && (b = v, q = h)
                    }
                    h = null;
                    f = [];
                    if (this.borders.copying && !0 === this.borders.copying.clear) {
                        for (r = p[1]; r <= p[3]; r++)
                            for (w = p[0]; w <= p[2]; w++)(!this.results || this.results && 0 <= this.results.indexOf(r)) && f.push({
                                x: w,
                                y: r,
                                value: _$_43fc[3],
                                style: _$_43fc[3]
                            });
                        e()
                    }
                    r = a + b[0].length - this.options.columns.length;
                    0 < r && this.insertColumn(r);
                    r = c + b.length - this.rows.length;
                    0 < r && this.insertRow(r);
                    m = g = a;
                    u = v = c;
                    x = null;
                    this.results && (h = this.results.indexOf(c));
                    for (r = 0; r < b.length; r++)
                        for (w = 0; w < b[r].length; w++) {
                            null !== l && null !== n && b[r][w][0] == _$_43fc[12] && (b[r][w] = S.shiftFormula(b[r][w], a - l, c - n));
                            m = a + w;
                            u = c + r;
                            this.results && (u = this.results[h + r]);
                            var y = {
                                x: m,
                                y: u,
                                value: b[r][w]
                            };
                            q && q[r] && q[r][w] && (y.style = q[r][w]);
                            f.push(y);
                            null === x && ua.isSelected.call(this, m, u, p) && (x = !0)
                        }
                    x && e();
                    f.length && (aa[_$_43fc[11]].call(this, f, null, this.parent.config.forceUpdateOnPaste ? !0 : !1), B.call(this.parent,
                        _$_43fc[196], this, f), this.updateSelectionFromCoords(g, v, m, u))
                }
            };
            k.download = function(a, c) {
                if (this.parent.config.allowExport) {
                    void 0 === c && (c = !0);
                    var b = _$_43fc[3];
                    if (1 == a || 1 == this.parent.config.includeHeadersOnDownload) b += this.getHeaders().replace(/\s+/gm, _$_43fc[132]) + _$_43fc[10];
                    b += k[_$_43fc[51]].call(this, null, c, this.options.csvDelimiter);
                    c = new Blob([_$_43fc[198] + b], {
                        type: _$_43fc[199]
                    });
                    window.navigator && window.navigator.msSaveOrOpenBlob ? window.navigator.msSaveOrOpenBlob(c, this.options.csvFileName +
                        _$_43fc[200]) : (a = document.createElement(_$_43fc[201]), c = URL.createObjectURL(c), a.href = c, a.setAttribute(_$_43fc[202], this.options.csvFileName + _$_43fc[200]), document.body.appendChild(a), a.click(), a.parentNode.removeChild(a))
                } else console.error(_$_43fc[197])
            };
            k.standardize = function(a) {
                a || (a = []);
                Array.isArray(a) || (a = [a]);
                void 0 == a[0] || Array.isArray(a[0]) || typeof a[0] == _$_43fc[84] || (a = [a]);
                for (var c = 0; c < a.length; c++) {
                    var b = {};
                    Array.isArray(a[c]) || void 0 == a[c].data ? b.data = a[c] : b = a[c];
                    b.row = void 0 == a[c].row ?
                        c : a[c].row;
                    a[c] = b
                }
                return a
            };
            return k
        }(),
        Ra = function() {
            var k = function(a) {
                a.getLabel = k[_$_43fc[51]];
                a.getLabelFromCoords = k[_$_43fc[51]]
            };
            k[_$_43fc[51]] = function(a, c, b) {
                if (typeof a == _$_43fc[17]) {
                    a = A.getCoordsFromColumnName(a);
                    var d = a[0];
                    c = a[1]
                } else d = a;
                return this.getProcessed(d, c, b)
            };
            return k
        }(),
        Ea = function() {
            var k = function(a) {
                a.getDefinedNames = k[_$_43fc[51]];
                a.setDefinedNames = k[_$_43fc[11]]
            };
            k[_$_43fc[11]] = function(a) {
                var c = Object.keys(a);
                if (c.length) {
                    for (var b = 0; b < c.length; b++) this.parent.config.definedNames[c[b]] =
                        a[c[b]];
                    G.call(this, _$_43fc[203], [a])
                }
            };
            k[_$_43fc[51]] = function(a) {
                var c = this.parent.config.definedNames;
                return a ? c[a] : c
            };
            k.updateAll = function(a, c) {
                var b, d = {};
                if (b = this.parent.config.definedNames) {
                    var e = Object.keys(b);
                    if (e.length) {
                        for (var f = this.getWorksheetName(), g = 0; g < e.length; g++) {
                            var h = S.update.call(this, b[e[g]], a, c, f);
                            h !== b[e[g]] && (d[e[g]] = h)
                        }
                        k[_$_43fc[11]].call(this, d)
                    }
                }
            };
            k.updateWorksheetName = function(a, c) {
                var b, d = {};
                if (b = this.parent.config.definedNames) {
                    var e = Object.keys(b);
                    if (e.length) {
                        for (var f =
                                0; f < e.length; f++) {
                            var g = S.updateWorksheetName(b[e[f]], a, c);
                            g !== b[e[f]] && (d[e[f]] = g)
                        }
                        k[_$_43fc[11]].call(this, d)
                    }
                }
            };
            return k
        }(),
        fa = function() {
            var k = [_$_43fc[204], _$_43fc[205], _$_43fc[206], _$_43fc[207], _$_43fc[208]],
                a = [_$_43fc[185], _$_43fc[151], _$_43fc[209], _$_43fc[210], _$_43fc[211], _$_43fc[212], _$_43fc[1], _$_43fc[213], _$_43fc[25], _$_43fc[214], _$_43fc[12], _$_43fc[215], _$_43fc[216]],
                c = [],
                b = function() {
                    if (0 < c.length) {
                        for (var f = 0; f < c.length; f++) da.destroy.call(this, c[f]);
                        c = []
                    }
                },
                d = function(f) {
                    b.call(this);
                    for (var g = 0, h = [], l, n = 0; n < f.children.length; n++)
                        if (l = f.children[n].getAttribute(_$_43fc[217])) {
                            h[l] || (h[l] = k[g++], 4 < g && (g = 0));
                            f.children[n].style.color = h[l];
                            if (-1 === l.indexOf(_$_43fc[135])) {
                                if (0 <= l.indexOf(_$_43fc[136])) {
                                    var q = l.split(_$_43fc[136]),
                                        p = A.getCoordsFromColumnName(q[0]);
                                    q = A.getCoordsFromColumnName(q[1])
                                } else q = p = A.getCoordsFromColumnName(l);
                                ua[_$_43fc[11]].call(this, p[0], p[1], q[0], q[1], null, l, h[l])
                            }
                            c.push(l)
                        }
                },
                e = function(f, g) {
                    f.classList.add(_$_43fc[92]);
                    f.classList.add(_$_43fc[218]);
                    f.classList.add(_$_43fc[219]);
                    f.classList.add(_$_43fc[220]);
                    f.setAttribute(_$_43fc[221], !0);
                    f.onclick = function(h) {
                        h = h.changedTouches && h.changedTouches[0] ? h.changedTouches[0].clientX : h.clientX;
                        var l = this.getBoundingClientRect();
                        24 > l.width - (h - l.left) && (e.current = f, f.classList.add(_$_43fc[222]))
                    };
                    f.onblur = function() {
                        f.worksheet && (b.call(f.worksheet), typeof f.onchange == _$_43fc[85] && f.onchange.call(f), f.worksheet = null)
                    };
                    f.onchange = g
                };
            e.current = null;
            e.parse = function(f) {
                for (var g = _$_43fc[3], h = _$_43fc[3], l = window.getSelection(), n = document.createRange(),
                        q = A.getCaretIndex.call(this.parent, f), p = S.tokenize(f.innerText.replace(/(\r\n|\n|\r)/gm, _$_43fc[3])), r = 0; r < p.length; r++) p[r] && S.tokenIdentifier.test(p[r].trim()) ? (r == p.length - 1 && (h = _$_43fc[223]), g += _$_43fc[224] + h + _$_43fc[225] + p[r].trim().replace(/\$/g, _$_43fc[3]) + _$_43fc[226] + p[r] + _$_43fc[227]) : g += p[r];
                f.innerHTML = g;
                g = null;
                for (r = 0; r < f.childNodes.length && !(g = f.childNodes[r], h = g.tagName ? g.innerText.length : g.length, 0 >= q - h); r++) q -= h;
                g && (g.tagName ? n.setStart(g.firstChild, q) : n.setStart(g, q), l.removeAllRanges(),
                    l.addRange(n));
                d.call(this, f)
            };
            e.close = function(f) {
                b.call(this);
                f ? f.classList.remove(_$_43fc[92]) : e.current && (typeof e.current.onchange == _$_43fc[85] && e.current.onchange.call(e.current), e.current.classList.remove(_$_43fc[222]), e.current.worksheet = null, e.current = null)
            };
            e.getName = function(f, g, h, l, n) {
                f = A.getColumnNameFromCoords(f, g);
                h = A.getColumnNameFromCoords(h, l);
                n.ctrlKey && (f = h);
                return f !== h ? (n = f + _$_43fc[136] + h, n = S.getTokensFromRange.call(this, n), S.getRangeFromTokens.call(this, n)) : f
            };
            e.range = function(f,
                g, h, l, n) {
                e.current.getAttribute(_$_43fc[228]) == _$_43fc[229] && (f = h, g = l);
                f = e.getName.call(this, f, g, h, l, n);
                g = document.createElement(_$_43fc[230]);
                g.innerText = f;
                g.setAttribute(_$_43fc[217], f);
                e.current.innerText = _$_43fc[3];
                e.current.appendChild(g);
                d.call(this, e.current);
                jSuites.focus(g);
                n.preventDefault()
            };
            e.update = function(f, g, h, l, n) {
                var q = this,
                    p = jSuites.getNode();
                if (p) {
                    var r = e.getName.call(this, f, g, h, l, n);
                    f = function(w) {
                        var m = document.createElement(_$_43fc[230]);
                        m.innerText = r;
                        m.setAttribute(_$_43fc[217],
                            r);
                        p.parentNode === q.parent.input && !1 === w ? p.parentNode.insertBefore(m, p.nextSibling) : q.parent.input.appendChild(m);
                        p = m
                    };
                    if (p.getAttribute(_$_43fc[217])) n.ctrlKey ? (q.parent.input.appendChild(document.createTextNode(_$_43fc[25])), f(!0)) : (p.innerText = r, p.setAttribute(_$_43fc[217], r));
                    else {
                        if (p.innerText !== _$_43fc[3] && -1 == a.indexOf(p.innerText.slice(-1)) || p.nextElementSibling && p.nextElementSibling.getAttribute(_$_43fc[217])) return !1;
                        f(!1)
                    }
                    jSuites.focus(p);
                    d.call(q, q.parent.input);
                    n.preventDefault();
                    return !0
                }
                return !1
            };
            e.onkeydown = function(f) {
                f.target.worksheet = this;
                if (f.ctrlKey) {
                    if (67 == f.which || 88 == f.which) f = window.getSelection().toString().replace(/(\r\n|\n|\r)/gm, _$_43fc[3]), navigator.clipboard ? navigator.clipboard.writeText(f) : (this.parent.textarea.value = f, this.parent.textarea.select(), document.execCommand(_$_43fc[192]))
                } else if (36 == f.which) {
                    var g = window.getSelection(),
                        h = document.createRange();
                    h.setStart(f.target, 0);
                    g.removeAllRanges();
                    g.addRange(h)
                } else 35 == f.which ? jSuites.focus(f.target) : 187 == f.which && !f.target.innerText.replace(/(\r\n|\n|\r)/gm,
                    _$_43fc[3]) && f.target.getAttribute(_$_43fc[94]) && f.target.removeAttribute(_$_43fc[94])
            };
            return e
        }(),
        S = function() {
            var k = new RegExp(/^(('.*?'!)|(\w*!))?(\$?[A-Z]+\$?[0-9]+)(:\$?[A-Z]+\$?[0-9]+)?$/i),
                a = function(h) {
                    h = c(h);
                    for (var l = [_$_43fc[185], _$_43fc[151], _$_43fc[209], _$_43fc[210], _$_43fc[211], _$_43fc[231]], n = [_$_43fc[212], _$_43fc[232], _$_43fc[1], _$_43fc[2], _$_43fc[213], _$_43fc[233], _$_43fc[25], _$_43fc[214], _$_43fc[12], _$_43fc[215], _$_43fc[216]], q = _$_43fc[3], p = [], r = 0, w = 0; w < h.length; w++) {
                        if (h[w] == _$_43fc[187] ||
                            h[w] == _$_43fc[133]) r = 0 == r ? 1 : 0;
                        0 == r && (-1 < l.indexOf(h[w]) || -1 < n.indexOf(h[w])) ? (q && (p.push(q), q = _$_43fc[3]), p.push(h[w])) : q += h[w]
                    }
                    q && p.push(q);
                    for (w = 0; w < p.length; w++) - 1 == p[w].indexOf(_$_43fc[187]) && -1 == p[w].indexOf(_$_43fc[133]) && (p[w] = p[w].replace(/\s/g, _$_43fc[3]));
                    return p
                },
                c = function(h, l) {
                    for (var n = _$_43fc[3], q = 0, p = [_$_43fc[12], _$_43fc[135], _$_43fc[216], _$_43fc[215]], r = 0; r < h.length; r++) h[r] == _$_43fc[187] && (q = 0 == q ? 1 : 0), 1 == q ? n += h[r] : (n += h[r].toUpperCase(), 1 == l && 0 < r && h[r] == _$_43fc[12] && -1 == p.indexOf(h[r -
                        1]) && -1 == p.indexOf(h[r + 1]) && (n += _$_43fc[12]));
                    return n
                },
                b = function(h) {
                    h.setFormula = b[_$_43fc[11]];
                    h.executeFormula = b.execute;
                    h.formula = []
                };
            b.run = function(h, l) {
                var n = _$_43fc[3],
                    q = Object.keys(l);
                if (q.length) {
                    for (var p = {}, r = 0; r < q.length; r++) {
                        var w = q[r].replace(/!/g, _$_43fc[27]);
                        0 < w.indexOf(_$_43fc[27]) && (w = w.split(_$_43fc[27]), p[w[0]] = !0)
                    }
                    w = Object.keys(p);
                    for (r = 0; r < w.length; r++) n += _$_43fc[234] + w[r] + _$_43fc[235];
                    for (r = 0; r < q.length; r++) w = q[r].replace(/!/g, _$_43fc[27]), jSuites.isNumeric(l[q[r]]) || (p = l[q[r]].match(/(('.*?'!)|(\w*!))?(\$?[A-Z]+\$?[0-9]*):(\$?[A-Z]+\$?[0-9]*)?/g)) &&
                        p.length && (l[q[r]] = e(p, l[q[r]])), n = 0 < w.indexOf(_$_43fc[27]) ? n + (w + _$_43fc[236] + l[q[r]] + _$_43fc[237]) : n + (_$_43fc[234] + w + _$_43fc[236] + l[q[r]] + _$_43fc[237])
                }
                h = h.replace(/\$/g, _$_43fc[3]);
                (p = h.match(/(('.*?'!)|(\w*!))?(\$?[A-Z]+\$?[0-9]*):(\$?[A-Z]+\$?[0-9]*)?/g)) && p.length && (h = e(p, h));
                h = h.replace(/!/g, _$_43fc[27]);
                return (new Function(n + _$_43fc[238] + h))()
            };
            b[_$_43fc[11]] = function() {};
            var d = function(h) {
                    h = c(h, !0);
                    return h.replace(/\$/g, _$_43fc[3])
                },
                e = function(h, l) {
                    for (var n = 0; n < h.length; n++) {
                        var q = b.getTokensFromRange.call(this,
                            h[n]);
                        l = l.replace(h[n], _$_43fc[213] + q.join(_$_43fc[25]) + _$_43fc[233])
                    }
                    return l
                },
                f = function(h, l, n) {
                    for (var q = this.config.definedNames, p = 0; p < h.length; p++) h[p] && q && q[h[p]] && (l = l.replace(h[p], q[h[p]]), n && (n[h[p]] = q[h[p]]));
                    return d(l)
                },
                g = function(h, l) {
                    var n;
                    if (n = this.records[l][h].a)
                        for (var q = 0; q < n.length; q++)
                            for (var p = 0; p < n[q].length; p++) n[q][p] && (n[q][p].innerText = _$_43fc[3], this.records[l + q][h + p].v = _$_43fc[3]);
                    this.records[l][h].a = []
                };
            b.execute = function(h, l, n, q, p) {
                if (1 == this.parent.processing) return null !==
                    l && null !== n && this.parent.queue.push([this, l, n]), _$_43fc[3];
                var r = [],
                    w = function(H, L, E, ba) {
                        if (void 0 != L && void 0 != E) {
                            var ra = A.getColumnNameFromCoords(L, E);
                            E = ba.getWorksheetName() + _$_43fc[27] + ra;
                            r[E] || (r[E] = 0);
                            r[E]++;
                            if (3 < r[E]) throw {
                                error: _$_43fc[239],
                                message: _$_43fc[240],
                                reference: E
                            };
                        } else ra = null;
                        p || (H = d(H));
                        var J = H,
                            U = J.match(/[A-Z_]+[A-Z0-9_\.]*/g);
                        U && U.length && (J = f.call(ba.parent, U, J, m));
                        (U = J.match(/(('.*?'!)|(\w*!))?(\$?[A-Z]+\$?[0-9]*):(\$?[A-Z]+\$?[0-9]*)?/g)) && U.length && (J = e.call(ba, U, J));
                        U = [];
                        J = a(J);
                        for (var R = 0; R < J.length; R++) J[R] && k.test(J[R]) && U.push(J[R]);
                        if (U) {
                            if (-1 < U.indexOf(ra)) throw {
                                error: _$_43fc[241],
                                message: _$_43fc[242]
                            };
                            for (J = 0; J < U.length; J++) {
                                R = U[J].split(_$_43fc[135]);
                                R[1] ? (L = R[1], R = D(R[0]), E = ba.getWorksheetName() + _$_43fc[27] + ra) : (L = R[0], R = ba, E = ra);
                                if (typeof R == _$_43fc[13] || typeof R.formula == _$_43fc[13]) throw {
                                    error: _$_43fc[243],
                                    message: _$_43fc[244]
                                };
                                E && (R.formula[L] || (R.formula[L] = []), 0 > R.formula[L].indexOf(E) && R.formula[L].push(E));
                                if (!m[U[J]]) {
                                    E = A.getCoordsFromColumnName(L);
                                    L = E[0];
                                    E = E[1];
                                    var oa = void 0 !== R.records[E][L].v ? R.records[E][L].v : K.call(R, L, E);
                                    m[U[J]] || ((_$_43fc[3] + oa).substr(0, 1) == _$_43fc[12] ? m[U[J]] = w(oa, L, E, R) : (oa ? oa != Number(oa) && (oa = _$_43fc[187] + oa + _$_43fc[187]) : oa = 0, m[U[J]] = oa))
                                }
                            }
                        }
                        return H.substr(1)
                    },
                    m = {};
                try {
                    if (h = w(h, l, n, this)) {
                        var v = p ? b.run(h, m) : t.formula(h, m, l, n, this);
                        if (!1 !== q && void 0 != l && void 0 != n)
                            if (v instanceof Date) this.records[n][l].v = jSuites.calendar.dateToNum(v);
                            else if (Array.isArray(v)) {
                            g.call(this, l, n);
                            q = [];
                            for (var u = null, x = 0; x < v.length; x++)
                                for (var y =
                                        0; y < v[x].length; y++) null === u && (0 < x && x > y && this.options.data[n + x][l + y] ? u = _$_43fc[245] : (this.records[n + x][l + y].v = v[x][y], this.records[n + x][l + y].element && (q[x] || (q[x] = []), q[x][y] = this.records[n + x][l + y].element, this.records[n + x][l + y].element.innerText = v[x][y])));
                            null === u ? (u = v[0][0], this.records[n][l].a = q) : g.call(this, l, n);
                            v = this.records[n][l].v = u
                        } else this.records[n][l].v = v;
                        return v
                    }
                } catch (H) {
                    return 1 == this.parent.config.debugFormulas && console.log(h, m, H), H && H.error ? H.error : _$_43fc[243]
                }
            };
            b.update = function(h,
                l, n, q) {
                var p = null,
                    r = null,
                    w = function(u) {
                        n && n[u] && (u = _$_43fc[246]);
                        l[u] && (u = l[u]);
                        return u
                    };
                h = c(h, !0);
                h = a(h);
                for (var m = 0; m < h.length; m++)
                    if (h[m] && k.test(h[m])) {
                        if (-1 == h[m].indexOf(_$_43fc[135])) {
                            if (r = h[m], p = _$_43fc[3], q) continue
                        } else if (r = h[m].split(_$_43fc[135]), p = r[0], r = r[1], q != p) continue;
                        if (0 <= r.indexOf(_$_43fc[136])) {
                            r = b.getTokensFromRange.call(this, r);
                            for (var v = 0; v < r.length; v++) r[v] = w(r[v]);
                            r = b.getRangeFromTokens.call(this, r)
                        } else -1 == r.indexOf(_$_43fc[247]) ? r = w(r) : (r = w(r.replace(/\$/g, _$_43fc[3])),
                            r = _$_43fc[247] + r.match(/[A-Z]+/g) + _$_43fc[247] + r.match(/[0-9]+/g));
                        p && (r = p + _$_43fc[135] + r);
                        h[m] = r
                    } return h.join(_$_43fc[3])
            };
            b.getChain = function(h) {
                if (void 0 === h) return [];
                var l = [],
                    n = [],
                    q = [],
                    p = function(m) {
                        if (0 <= m.indexOf(_$_43fc[27])) {
                            var v = m.split(_$_43fc[27]),
                                u = v[0],
                                x = v[1];
                            v = D(v[0])
                        } else v = this, u = v.getWorksheetName(), x = m;
                        if (v.formula[x] && !q[m]) {
                            var y = !1;
                            q[m] = !0;
                            for (m = 0; m < v.formula[x].length; m++) {
                                var H = v.formula[x][m]; - 1 == H.indexOf(_$_43fc[27]) && (H = u + _$_43fc[27] + H);
                                if (!n[H]) {
                                    var L = v.getValue(H);
                                    M(L) ?
                                        l.push([H, L, b.getTokens.call(v, L, u)]) : (y = !0, v.formula[x][m] = null);
                                    p.call(v, H);
                                    n[H] = !0
                                }
                            }
                            1 == y && (v.formula[x] = v.formula[x].filter(function(E) {
                                return null != E
                            }))
                        }
                    },
                    r = Object.keys(h);
                for (h = 0; h < r.length; h++) p.call(this, r[h]);
                r = 0;
                for (h = l.length - 1; 0 <= h; h--) {
                    for (var w = 0; w < h; w++)
                        if (0 <= l[w][2].indexOf(l[h][0])) {
                            l.splice(w, 0, l.splice(h, 1)[0]);
                            h = l.length;
                            break
                        } r++;
                    if (1E6 < r) {
                        console.error(_$_43fc[248]);
                        break
                    }
                }
                n = [];
                for (h = 0; h < l.length; h++) n[l[h][0]] = l[h][1];
                return n
            };
            b.updateAll = function(h, l) {
                var n, q;
                Ea.updateAll.call(this,
                    h, l);
                var p = Object.keys(this.formula);
                var r = [];
                var w = [];
                for (n = 0; n < p.length; n++) {
                    var m = p[n];
                    var v = this.formula[m];
                    if (l && l[m])
                        for (var u = 0; u < v.length; u++) h[v[u]] && (v[u] = h[v[u]]), w[v[u]] = !0;
                    else {
                        var x = [];
                        for (u = 0; u < v.length; u++) l && l[v[u]] || (h[v[u]] && (v[u] = h[v[u]]), w[v[u]] = !0, x.push(v[u]));
                        x.length && (h[m] && (m = h[m]), r[m] = x)
                    }
                }
                this.formula = r;
                x = this.getWorksheetName();
                for (var y = 0; y < t.spreadsheet.length; y++)
                    for (var H = t.spreadsheet[y].worksheets, L = 0; L < H.length; L++)
                        if (H[L].getWorksheetName() !== x)
                            for (p = Object.keys(H[L].formula),
                                n = 0; n < p.length; n++) {
                                r = [];
                                v = H[L].formula[p[n]];
                                for (u = 0; u < v.length; u++) m = v[u].split(_$_43fc[27]), m[0] == x && m[1] ? l && l[m[1]] || (h[m[1]] && (m[1] = h[m[1]]), r.push(m[0] + _$_43fc[27] + m[1])) : r.push(v[u]);
                                H[L].formula[p[n]] = r
                            }
                p = [];
                m = Object.keys(w);
                for (u = 0; u < m.length; u++) 0 <= m[u].indexOf(_$_43fc[27]) ? (v = m[u].split(_$_43fc[27]), r = D(v[0]), n = v[1], x = this.getWorksheetName(), v = v[0]) : (r = this, n = m[u], x = null, v = this.getWorksheetName()), (q = r.getValue(n)) && M(q) && (r = b.update.call(r, q, h, l, x), r != q && (w[m[u]] = r, n = A.getCoordsFromColumnName(n),
                    p[v] || (p[v] = []), p[v].push({
                        x: n[0],
                        y: n[1],
                        value: r,
                        force: !0
                    })));
                m = Object.keys(p);
                for (h = 0; h < m.length; h++) r = D(m[h]), p[m[h]] = aa.applyValues.call(r, p[m[h]]), aa.setValueChained.call(r, p[m[h]]), G.call(r, _$_43fc[107], {
                    data: p[m[h]]
                })
            };
            b.updateWorksheetName = function(h, l, n) {
                for (var q = a(h), p = 0; p < q.length; p++) h = q[p].split(_$_43fc[135]), h[1] && (h[0] = h[0].replace(new RegExp(_$_43fc[133], _$_43fc[188]), _$_43fc[3]), h[0].toUpperCase() == l.toUpperCase() && (h[0] = n, 0 <= h[0].indexOf(_$_43fc[132]) && (h[0] = _$_43fc[133] + h[0] + _$_43fc[133]),
                    q[p] = h.join(_$_43fc[135])));
                return q.join(_$_43fc[3])
            };
            b.updateWorksheetNames = function(h, l) {
                for (var n, q, p = 0; p < t.spreadsheet.length; p++)
                    for (var r = [], w = t.spreadsheet[p].worksheets, m = 0; m < w.length; m++) {
                        for (var v = 0; v < w[m].options.data.length; v++)
                            for (var u = 0; u < w[m].options.data[v].length; u++)(n = w[m].options.data[v][u]) && M(n) && (q = b.updateWorksheetName(n, h, l), q != n && r.push({
                                x: u,
                                y: v,
                                value: q,
                                force: !0
                            }));
                        r = aa.applyValues.call(w[m], r);
                        G.call(w[m], _$_43fc[107], {
                            data: r
                        })
                    }
            };
            b.shiftFormula = function(h, l, n) {
                var q = a(h,
                    !0);
                h = function(m) {
                    if (-1 == m.indexOf(_$_43fc[135])) {
                        m = A.getCoordsFromColumnName(m);
                        var v = _$_43fc[3]
                    } else v = q[p].split(_$_43fc[135]), m = A.getCoordsFromColumnName(v[1]), v = v[0] + _$_43fc[135];
                    var u = m[0] + l;
                    m = m[1] + n;
                    return m = 0 > u || 0 > m ? _$_43fc[246] : v + A.getColumnNameFromCoords(u, m)
                };
                for (var p = 0; p < q.length; p++)
                    if (k.test(q[p]) && -1 == q[p].indexOf(_$_43fc[247])) {
                        var r = -1 == q[p].indexOf(_$_43fc[135]) ? [_$_43fc[3], q[p]] : q[p].split(_$_43fc[135]);
                        if (-1 == r[1].indexOf(_$_43fc[136])) r[1] = h(r[1]);
                        else {
                            var w = r[1].split(_$_43fc[136]);
                            r[1] = h(w[0]) + _$_43fc[136] + h(w[1])
                        }
                        q[p] = r[0] ? r.join(_$_43fc[135]) : r[1]
                    } return q.join(_$_43fc[3])
            };
            b.getTokensFromRange = function(h) {
                var l = this,
                    n = _$_43fc[3],
                    q = _$_43fc[3];
                0 <= h.indexOf(_$_43fc[27]) ? q = _$_43fc[27] : 0 <= h.indexOf(_$_43fc[135]) && (q = _$_43fc[135]);
                h = h.replace(/\$/g, _$_43fc[3]);
                q && (n = h.split(q), l = D(n[0]), h = n[1], n = n[0] + q);
                q = [];
                h = h.split(_$_43fc[136]);
                var p = A.getCoordsFromColumnName(h[0]),
                    r = A.getCoordsFromColumnName(h[1]);
                if (p[0] <= r[0]) {
                    h = p[0];
                    var w = r[0]
                } else h = r[0], w = p[0];
                if (null === p[1] && null ==
                    r[1]) {
                    var m = 0;
                    p = 0;
                    l.options && l.options.data && (p = l.options.data.length - 1)
                } else p[1] <= r[1] ? (m = p[1], p = r[1]) : (m = r[1], p = p[1]);
                for (l = m; l <= p; l++)
                    for (m = h; m <= w; m++) q.push(n + A.getColumnNameFromCoords(m, l));
                return q
            };
            b.getRangeFromTokens = function(h) {
                h = h.filter(function(q) {
                    return q != _$_43fc[246]
                });
                for (var l = _$_43fc[3], n = 0; n < h.length; n++) 0 <= h[n].indexOf(_$_43fc[27]) && (l = h[n].split(_$_43fc[27]), h[n] = l[1], l = l[0]);
                l && (l += _$_43fc[27]);
                h.sort(function(q, p) {
                    q = A.getCoordsFromColumnName(q);
                    p = A.getCoordsFromColumnName(p);
                    return q[1] > p[1] ? 1 : q[1] < p[1] ? -1 : q[0] > p[0] ? 1 : q[0] < p[0] ? -1 : 0
                });
                return h.length ? l + (h[0] + _$_43fc[136] + h[h.length - 1]) : _$_43fc[246]
            };
            b.getTokens = function(h, l) {
                var n = d(h);
                h.replace(/!/g, _$_43fc[27]);
                (h = n.match(/[A-Z_]+[A-Z0-9_\.]*/g)) && h.length && (n = f.call(this.parent, h, n));
                (h = n.match(/('.*?'?|\w*\.)?(\$?[A-Z]+\$?[0-9]*):(\$?[A-Z]+\$?[0-9]*)?/g)) && h.length && (n = e.call(this, h, n));
                h = n.match(/('.*?'?|\w*\.)?(\$?[A-Z]+\$?[0-9]+)(:\$?[A-Z]+\$?[0-9]+)?/g);
                for (n = 0; n < h.length; n++) - 1 == h[n].indexOf(_$_43fc[27]) && l &&
                    (h[n] = l + _$_43fc[27] + h[n]);
                return h
            };
            b.tokenIdentifier = k;
            b.tokenize = a;
            return b
        }(),
        aa = function() {
            var k = function(a) {
                a.name = V;
                a.value = K;
                a.getValue = k[_$_43fc[51]];
                a.getValueFromCoords = k.getFromCoords;
                a.setValue = k[_$_43fc[11]];
                a.setValueFromCoords = k.setFromCoords;
                a.setCheckRadioValue = k.setCheckRadio;
                a.getProcessed = k.processed
            };
            k[_$_43fc[51]] = function(a, c) {
                var b = this;
                if (typeof a == _$_43fc[84]) {
                    var d = a.x;
                    a = a.y
                } else 0 <= a.indexOf(_$_43fc[27]) && (b = a.split(_$_43fc[27]), a = b[1], b = D(b[0])), a = A.getCoordsFromColumnName(a),
                    d = a[0], a = a[1];
                return k.getFromCoords.call(b, d, a, c)
            };
            k.getFromCoords = function(a, c, b) {
                var d = null;
                null != a && null != c && (d = b ? k.processed.call(this, a, c) : K.call(this, a, c));
                return d
            };
            k[_$_43fc[11]] = function(a, c, b) {
                if (!N.call(this.parent, this)) return !1;
                var d = null,
                    e = null,
                    f = null,
                    g = [];
                b = b ? !0 : !1;
                var h = function(q, p) {
                    var r = null;
                    typeof q == _$_43fc[17] ? (f = A.getCoordsFromColumnName(q), d = f[0], e = f[1]) : q.tagName ? (d = q.getAttribute(_$_43fc[42]), e = q.getAttribute(_$_43fc[43])) : q.element && q.element.tagName ? (d = q.element.getAttribute(_$_43fc[42]),
                        e = q.element.getAttribute(_$_43fc[43])) : (d = q.x, e = q.y, void 0 !== q.value && (p = q.value), void 0 !== q.style && (r = q.style));
                    q = {
                        x: d,
                        y: e,
                        value: p,
                        force: b
                    };
                    null !== r && (q.style = r);
                    g.push(q)
                };
                if (a && Array.isArray(a))
                    for (var l = 0; l < a.length; l++) h(a[l], c);
                else if (typeof a == _$_43fc[84] && void 0 == a.x && void 0 == a.tagName)
                    for (c = Object.keys(a), l = 0; l < c.length; l++) h(c[l], a[c[l]]);
                else h(a, c);
                if (g.length) {
                    g = k.applyValues.call(this, g);
                    this.parent.config.parseFormulas && k.setValueChained.call(this, g);
                    T.update.call(this, !0);
                    this.refreshBorders();
                    var n = this;
                    setTimeout(function() {
                        da.refresh.call(n)
                    }, 250);
                    if (g.length) {
                        Q.call(this.parent, {
                            worksheet: n,
                            action: _$_43fc[79],
                            records: g,
                            selection: this.selectedCell
                        });
                        a = this.getPrimaryKey();
                        if (!1 !== a)
                            for (l = 0; l < g.length; l++) g[l].x === a && g[l].value && O.call(this, g[l]);
                        G.call(this, _$_43fc[79], {
                            data: g
                        });
                        B.call(this.parent, _$_43fc[249], this, g)
                    }
                }
            };
            k.setFromCoords = function(a, c, b, d) {
                var e = [];
                e.push({
                    x: a,
                    y: c,
                    value: b
                });
                k[_$_43fc[11]].call(this, e, null, d)
            };
            k.setCheckRadio = function() {
                for (var a = [], c = this.getSelected(),
                        b = 0; b < c.length; b++) {
                    var d = c[b].x,
                        e = c[b].y;
                    if (!this.options.columns[d].readOnly && this.options.columns[d].type == _$_43fc[250]) {
                        var f = K.call(this, d, e) ? !1 : !0;
                        a.push({
                            x: d,
                            y: e,
                            value: f
                        })
                    }
                }
                a.length && k[_$_43fc[11]].call(this, a)
            };
            k.applyValues = function(a) {
                for (var c, b, d = this.options.style || {}, e = [], f = {}, g = 0; g < a.length; g++)
                    if (c = a[g], b = this.updateCell(c.x, c.y, c.value, c.force), void 0 !== b.value) {
                        if (void 0 !== c.style) {
                            var h = A.getColumnNameFromCoords(c.x, c.y);
                            b.style = f[h] = c.style;
                            b.oldStyle = d && d[h] ? d[h] : _$_43fc[193]
                        }
                        e.push(b)
                    } 0 <
                    Object.keys(f).length && Ma.update.call(this, f);
                return e
            };
            k.processed = function(a, c, b) {
                var d = K.call(this, a, c);
                if (null === d) return null;
                var e = Z[_$_43fc[51]].call(this, a, c),
                    f = e[1];
                f.type != _$_43fc[49] && f.type != _$_43fc[251] && f.type != _$_43fc[252] && f.type != _$_43fc[253] && f.type != _$_43fc[254] && 0 != f.process && (!0 === b || f.type != _$_43fc[255] && f.type != _$_43fc[256] && f.type != _$_43fc[257]) && (f.type == _$_43fc[250] ? d = e[0][_$_43fc[51]](f, d, b) : this.records[c][a].element ? d = 1 == this.parent.config.stripHTML ? this.records[c][a].element.innerText :
                    this.records[c][a].element.innerHTML : ((_$_43fc[3] + d).substr(0, 1) == _$_43fc[12] && (d = this.executeFormula(d, a, c, !1)), e[0] && typeof e[0][_$_43fc[51]] == _$_43fc[85] && (d = e[0][_$_43fc[51]](f, d, b))));
                return d
            };
            k.setValueChained = function(a) {
                for (var c, b = [], d, e = 0; e < a.length; e++) c = a[e], d = A.getColumnNameFromCoords(c.x, c.y), b[d] = c.value;
                b = S.getChain.call(this, b);
                a = Object.keys(b);
                for (e = 0; e < a.length; e++) 0 <= a[e].indexOf(_$_43fc[27]) ? (c = a[e].split(_$_43fc[27]), d = c[1], c = D(c[0])) : (d = a[e], c = this), d = A.getCoordsFromColumnName(d),
                    I.update.call(c, d[0], d[1], b[a[e]], !0)
            };
            return k
        }(),
        I = function() {
            var k = function(a) {
                a.getCell = k[_$_43fc[51]];
                a.getCellFromCoords = k[_$_43fc[51]];
                a.updateCell = k.update;
                a.updateCells = k.updateAll;
                a.getSelected = k.selected;
                a.getCells = k.getCells;
                a.setCells = k.setCells;
                a.isAttached = k.attached
            };
            k.attached = function(a, c) {
                return this.records[c] && this.records[c][a] && this.records[c][a].element && this.records[c][a].element.parentNode && this.records[c][a].element.parentNode.parentNode ? !0 : !1
            };
            k[_$_43fc[51]] = function(a,
                c, b) {
                typeof a == _$_43fc[17] && (c = A.getCoordsFromColumnName(a), a = c[0], c = c[1]);
                a = parseInt(a);
                c = parseInt(c);
                if (this.records[c] && this.records[c][a] && this.records[c][a].element) return this.records[c][a].element;
                var d = K.call(this, a, c);
                null === d && (d = _$_43fc[3]);
                return I.create.call(this, a, c, d, b)
            };
            k.getFromCoords = function(a, c, b) {
                if (!a) return null;
                var d = [],
                    e = a[0],
                    f = a[1],
                    g = a[2];
                a = a[3];
                for (b && (b = this.results ? A.invert(this.results) : null); f <= a; f++)
                    for (var h = e; h <= g; h++)
                        if (!b || b[f]) c ? d.push(A.getColumnNameFromCoords(h,
                            f)) : d.push(this.records[f][h]);
                return d
            };
            k.selected = function(a, c) {
                return k.getFromCoords.call(this, this.getHighlighted(), a, c)
            };
            k.getSelectedColumns = function() {
                var a = [],
                    c = this.getHighlighted();
                if (c)
                    for (var b = c[0]; b <= c[2]; b++) a.push(b);
                return a
            };
            k.updateAll = function(a) {
                for (var c = this.getPrimaryKey(), b = [], d = 0; d < a.length; d++) void 0 !== a[d].id ? (O.call(this, a[d]), b.push({
                    id: a[d].id,
                    y: a[d].y
                }), !1 !== c && k.update.call(this, c, a[d].y, a[d].id, !0)) : b.push(k.update.call(this, a[d].x, a[d].y, a[d].value, a[d].force));
                G.call(this, _$_43fc[106], [b])
            };
            k.update = function(a, c, b, d) {
                var e = this.records[c];
                if (e = e && e[a] ? e[a] : null)
                    if (e = e.element, !d && e && 1 == e.classList.contains(_$_43fc[18])) d = {
                        x: a,
                        y: c
                    };
                    else {
                        d = B.call(this.parent, _$_43fc[258], this, e, a, c, b);
                        void 0 !== d && (b = d);
                        d = Z[_$_43fc[51]].call(this, a, c);
                        typeof d[0].updateCell == _$_43fc[85] && (d = d[0].updateCell(e, b, a, c, this, d[1]), void 0 !== d && (b = d));
                        d = {
                            x: a,
                            y: c
                        };
                        var f = K.call(this, a, c);
                        f !== b && (b = K.call(this, a, c, b), d.oldValue = f, d.value = b, e && k.applyOverflow.call(this, e, a, c), B.call(this.parent,
                            _$_43fc[259], this, e, a, c, d.value, d.oldValue))
                    }
                else return !1;
                return d
            };
            k.create = function(a, c, b, d) {
                var e = document.createElement(_$_43fc[147]);
                e.setAttribute(_$_43fc[42], a);
                e.setAttribute(_$_43fc[43], c);
                var f = Z[_$_43fc[51]].call(this, a, c);
                f[0] && typeof f[0].createCell == _$_43fc[85] && (f = f[0].createCell(e, b, a, c, this, f[1]), void 0 !== f && (b = f));
                K.call(this, a, c, b);
                this.records[c][a] || ka.cell.call(this, a, c, b);
                this.records[c][a].element = e;
                !b && this.records[c][a].v && (e.innerText = this.records[c][a].v);
                k.applyProperties.call(this,
                    e, a, c, d);
                k.applyOverflow.call(this, e, a, c);
                B.call(this.parent, _$_43fc[260], this, e, a, c, b);
                return e
            };
            k.getAttributes = function(a, c, b) {
                b[a] || (b[a] = {});
                a == _$_43fc[261] ? b[a][c] = this.getValue(c) : a == _$_43fc[262] ? this.merged.cells && (c = this.merged.cells[c]) && (b[a][c] = this.options.mergeCells[c]) : this.options[a] && this.options[a][c] && (b[a][c] = this.options[a][c])
            };
            k.setAttributes = function(a) {
                a && a.style && this.setStyle(a.style);
                a && a.comments && this.setComments(a.comments);
                a && a.meta && this.setMeta(a.meta);
                a && a.cells &&
                    this.setCells(a.cells);
                a && a.mergeCells && this.setMerge(a.mergeCells);
                a && a.values && k.batch.call(this, a.values);
                a && a.formulas && k.batch.call(this, a.formulas, !0)
            };
            k.applyProperties = function(a, c, b, d) {
                if (!d) {
                    d = A.getColumnNameFromCoords(c, b);
                    var e = this.options.style,
                        f = this.options.comments,
                        g = this.options.cells,
                        h = this.options.rows;
                    if (e && e[d]) {
                        var l = (l = a.getAttribute(_$_43fc[98])) ? l + (_$_43fc[263] + e[d]) : e[d];
                        a.setAttribute(_$_43fc[98], l)
                    }
                    f && f[d] && a.setAttribute(_$_43fc[264], f[d]);
                    g && g[d] && (1 == g[d].readOnly &&
                        a.classList.add(_$_43fc[18]), !1 === g[d].locked && !0 === this.options.selectUnLockedCells && a.classList.add(_$_43fc[265]));
                    h && h[b] && 1 == h[b].readOnly && a.classList.add(_$_43fc[18])
                }
                f = this.options.columns[c];
                a.style.textAlign || (a.style.textAlign = f.align ? f.align : this.options.defaultColAlign);
                !1 === this.records[b][c].readonly || 1 != this.records[b][c].readonly && 1 != f.readOnly && 1 != f.readonly || a.classList.add(_$_43fc[18])
            };
            k.applyOverflow = function(a, c, b) {
                1 == this.options.textOverflow && 0 < c && (c == this.options.columns.length -
                    1 && (a.style.overflow = _$_43fc[49]), this.records[b][c - 1] && this.records[b][c - 1].element && (this.records[b][c - 1].element.style.overflow = !a.innerText && this.headers[c - 1] && this.headers[c - 1].offsetWidth ? _$_43fc[3] : _$_43fc[49]))
            };
            k.batch = function(a, c) {
                var b = [],
                    d = Object.keys(a);
                if (d.length)
                    for (var e = 0; e < d.length; e++) {
                        if (0 <= d[e].indexOf(_$_43fc[27])) {
                            var f = d[e].split(_$_43fc[27]);
                            var g = D(f[0]);
                            var h = f[1];
                            f = f[0]
                        } else g = this, h = d[e], f = this.getWorksheetName();
                        h = A.getCoordsFromColumnName(h);
                        h = g.updateCell(h[0], h[1],
                            a[d[e]], !0);
                        b[f] || (b[f] = []);
                        b[f].push(h)
                    }
                if (1 == c && (d = Object.keys(b), d.length))
                    for (e = 0; e < d.length; e++) g = D(d[e]), G.call(g, _$_43fc[107], b[d[e]])
            };
            k.getCells = function(a) {
                return a ? this.options.cells[a] : this.options.cells
            };
            k.setCells = function(a, c) {
                if (!N.call(this.parent, this)) return !1;
                if (typeof a == _$_43fc[17]) this.options.cells[a] = c;
                else {
                    c = Object.keys(a);
                    for (var b = 0; b < c.length; b++) this.options.cells[c[b]] = a[c[b]]
                }
            };
            return k
        }(),
        wa = function() {
            var k = function(c, b) {
                    Array.isArray(b) || (b = [b]);
                    for (var d, e = c ? _$_43fc[3] :
                            _$_43fc[40], f = Object.keys(b), g = 0; g < f.length; g++)
                        if (d = this.rows[b[f[g]]]) d.element && (d.element.style.display = e), d.visible = c;
                    this.resetBorders();
                    C.refresh.call(this)
                },
                a = function(c) {
                    c.getRow = a[_$_43fc[51]];
                    c.moveRow = a.move;
                    c.insertRow = a.add;
                    c.deleteRow = a.del;
                    c.getSelectedRows = a.selected;
                    c.showRow = k.bind(c, !0);
                    c.hideRow = k.bind(c, !1)
                };
            a[_$_43fc[51]] = function(c) {
                return this.rows[c]
            };
            a.attached = function(c) {
                return this.rows[c] && this.rows[c].element && this.rows[c].element.parentNode ? !0 : !1
            };
            a.selected = function(c) {
                var b = [],
                    d = this.getHighlighted();
                if (d)
                    for (var e = d[1]; e <= d[3]; e++) c ? b.push(e) : b.push(this.rows[e].element);
                return b
            };
            a.create = function(c, b) {
                if (this.rows[c] && this.rows[c].element) return this.rows[c].element;
                this.rows[c] || ka.row.call(this, c);
                var d = document.createElement(_$_43fc[145]);
                d.setAttribute(_$_43fc[43], c);
                this.rows[c].element = d;
                var e = null;
                this.options.rows[c] && !b && (this.options.rows[c].style && d.setAttribute(_$_43fc[98], this.options.rows[c].style), this.options.rows[c].height && (d.style.height = parseInt(this.options.rows[c].height) +
                    _$_43fc[44]), this.options.rows[c].title && (e = this.options.rows[c].title), 0 == this.options.rows[c].visible && (this.rows[c].visible = !1), this.options.rows[c].id && (this.rows[c].id = this.options.rows[c].id));
                B.call(this.parent, _$_43fc[266], this, c, d);
                0 == this.rows[c].visible && (d.style.display = _$_43fc[40]);
                e || (e = parseInt(c + 1));
                b = document.createElement(_$_43fc[147]);
                b.innerHTML = e;
                b.setAttribute(_$_43fc[43], c);
                b.className = _$_43fc[267];
                d.appendChild(b);
                return d
            };
            a.move = function(c, b) {
                if (!N.call(this.parent, this)) return !1;
                c = parseInt(c);
                b = parseInt(b);
                if (!this.rows[b] || !this.rows[c]) return console.error(_$_43fc[268]), !1;
                if (0 < Object.keys(this.getMerge()).length) {
                    var d = null;
                    this.merged.rows[c] ? d = !0 : this.merged.rows[b] && (c > b ? this.merged.rows[b - 1] && (d = !0) : this.merged.rows[b + 1] && (d = !0));
                    d && this.destroyMerged()
                }
                this.resetBorders();
                0 <= Array.prototype.indexOf.call(this.tbody.children, this.rows[b].element) ? (d = C.renderRow.call(this, c), c > b ? this.tbody.insertBefore(d, this.rows[b].element) : this.tbody.insertBefore(d, this.rows[b].element.nextSibling)) :
                    this.rows[c].element && (this.tbody.removeChild(this.rows[c].element), C.refresh.call(this));
                this.rows.splice(b, 0, this.rows.splice(c, 1)[0]);
                this.records.splice(b, 0, this.records.splice(c, 1)[0]);
                this.options.data.splice(b, 0, this.options.data.splice(c, 1)[0]);
                0 < this.options.pagination && this.tbody.children.length != this.options.pagination && this.page(this.pageNumber);
                G.call(this, _$_43fc[67], {
                    f: c,
                    t: b
                });
                T.references.call(this);
                T.update.call(this);
                Q.call(this.parent, {
                    worksheet: this,
                    action: _$_43fc[67],
                    oldValue: c,
                    newValue: b
                });
                B.call(this.parent, _$_43fc[269], this, c, b)
            };
            a.add = function(c, b, d, e) {
                if (!N.call(this.parent, this)) return !1;
                if (!this.options.allowInsertRow) return console.error(_$_43fc[270]), !1;
                var f = [];
                if (0 < c) var g = parseInt(c);
                else g = 1, Array.isArray(c) && !e && (Array.isArray(c[0]) ? e = c : (e = [], e.push(c)));
                e = Ba.standardize(e);
                d = d ? !0 : !1;
                var h = this.options.data ? this.options.data.length - 1 : 0;
                if (void 0 == b || b > h || 0 > b) b = h, d = !1;
                g || (g = 1);
                if (!1 === B.call(this.parent, _$_43fc[271], this, b, g, d)) return console.log(_$_43fc[272]),
                    !1;
                this.resetBorders();
                var l = d ? b : b + 1,
                    n = 0;
                c = [];
                for (var q = [], p = this.rows.splice(l), r = this.options.data ? this.options.data.splice(l) : [], w = this.records.splice(l), m = l; m < g + l; m++) {
                    var v = e[n] && e[n].id ? e[n].id : pa.next.call(this);
                    ka.row.call(this, m, v);
                    var u = this.dataType ? {} : [];
                    for (var x = 0; x < this.options.columns.length; x++) {
                        var y = V.call(this, x);
                        u[y] = _$_43fc[3];
                        e && e[n] && e[n].data && (typeof e[n].data[y] !== _$_43fc[13] ? u[y] = e[n].data[y] : typeof e[n].data[x] !== _$_43fc[13] && (u[y] = e[n].data[x]));
                        f.push({
                            x,
                            y: m,
                            value: u[y],
                            force: !0
                        })
                    }
                    q.push({
                        id: v,
                        data: u
                    });
                    n++
                }
                this.rows = this.rows.concat(p);
                this.records = this.records.concat(w);
                this.options.data = this.options.data.concat(r);
                this.results && 0 < this.results.length && (h = this.results[this.results.length - 1]);
                e = !1;
                p[0] ? 0 <= Array.prototype.indexOf.call(this.tbody.children, p[0].element) && (e = !0) : this.rows[h] ? this.rows[h].element && 0 <= Array.prototype.indexOf.call(this.tbody.children, this.rows[h].element) && (e = !0) : e = !0;
                for (m = l; m < g + l; m++) this.results && (this.rows[m].results = !0), 1 == e && (h = p[0] ?
                    p[0].element : null, y = C.renderRow.call(this, m, null, null, !0), h ? this.tbody.insertBefore(y, h) : this.tbody.appendChild(y), 0 < this.options.pagination ? Array.prototype.indexOf.call(this.tbody.children, y) >= this.options.pagination && (e = !1) : C.limited.call(this) && (C.isVisible.call(this, y.children[1]) || (e = !1))), c.push(this.records[m]);
                this.merged.rows[l] && X.updateConfig.call(this, 1, 1, l - 1, g);
                G.call(this, _$_43fc[63], {
                    numOfRows: g,
                    rowNumber: b,
                    insertBefore: d ? 1 : 0,
                    data: q
                });
                l = this.tbody.firstChild.getAttribute(_$_43fc[43]);
                this.results && (l = this.results.indexOf(parseInt(l)), this.results = sa.refresh.call(this));
                T.references.call(this);
                f.length && aa.applyValues.call(this, f);
                0 < this.options.pagination ? qa[_$_43fc[11]].call(this, this.pageNumber) : (this.results && (l = this.results[l]), this.tbody.textContent = _$_43fc[3], C[_$_43fc[46]].call(this, l));
                T.update.call(this);
                P.call(this);
                da.refresh.call(this);
                Q.call(this.parent, {
                    worksheet: this,
                    action: _$_43fc[63],
                    rowNumber: b,
                    numOfRows: g,
                    insertBefore: d,
                    data: q
                });
                B.call(this.parent, _$_43fc[273],
                    this, b, g, c, d)
            };
            a.del = function(c, b) {
                if (!N.call(this.parent, this)) return !1;
                if (!this.options.allowDeleteRow) return console.error(_$_43fc[274]), !1;
                var d = null,
                    e = {},
                    f = [],
                    g = 0;
                void 0 == c && (b = this.getSelectedRows(), b[0] ? (c = parseInt(b[0].getAttribute(_$_43fc[43])), b = b.length) : (c = this.options.data.length - 1, b = 1));
                d = this.options.data.length - 1;
                if (void 0 == c || c > d || 0 > c) c = d;
                b || (b = 1);
                c + b >= this.options.data.length && (b = this.options.data.length - c, b >= this.options.data.length && (b = this.options.data.length, this.resetSelection()));
                if (!1 === B.call(this.parent, _$_43fc[275], this, c, b)) return console.log(_$_43fc[276]), !1;
                if (-1 < parseInt(c) && 0 < parseInt(b)) {
                    this.resetBorders();
                    for (var h = c; h < c + b; h++)
                        for (var l = 0; l < this.options.columns.length; l++) d = A.getColumnNameFromCoords(l, h), I.getAttributes.call(this, _$_43fc[277], d, e), I.getAttributes.call(this, _$_43fc[98], d, e), I.getAttributes.call(this, _$_43fc[278], d, e), I.getAttributes.call(this, _$_43fc[279], d, e), I.getAttributes.call(this, _$_43fc[262], d, e), I.getAttributes.call(this, _$_43fc[261], d,
                            e), f[d] = !0;
                    if (d = S.getChain.call(this, e.values)) e.formulas = d;
                    for (h = c; h < c + b; h++) 0 <= Array.prototype.indexOf.call(this.tbody.children, this.rows[h].element) && (this.rows[h].element.parentNode.removeChild(this.rows[h].element), g++);
                    d = [];
                    for (h = c; h < c + b; h++) d.push({
                        id: this.getRowId(h),
                        row: h,
                        data: this.getRowData(h)
                    });
                    this.rows.splice(c, b);
                    h = this.records.splice(c, b);
                    this.options.data.splice(c, b);
                    X.updateConfig.call(this, 1, 0, c, b);
                    G.call(this, _$_43fc[64], {
                        rowNumber: c,
                        numOfRows: b,
                        data: d
                    });
                    T.references.call(this,
                        f);
                    this.results && (this.results = sa.refresh.call(this));
                    if (0 < this.options.pagination) this.page(this.pageNumber, function() {
                        if (0 == this.tbody.children.length) {
                            var n = this.whichPage(this.options.data.length - 1);
                            this.page(n)
                        }
                    });
                    else if (C.limited.call(this) && 0 < g)
                        if (0 == this.tbody.children.length) this[_$_43fc[46]](c);
                        else C.refresh.call(this);
                    T.update.call(this, !0);
                    this.resetBorders();
                    Q.call(this.parent, {
                        worksheet: this,
                        action: _$_43fc[64],
                        rowNumber: c,
                        numOfRows: b,
                        insertBefore: 1,
                        data: d,
                        attributes: e
                    });
                    B.call(this.parent,
                        _$_43fc[280], this, c, b, h, d, e)
                }
            };
            return a
        }(),
        Da = function() {
            var k = function(c, b) {
                    Array.isArray(b) || (b = [b]);
                    for (var d, e, f = Object.keys(b), g = 0; g < f.length; g++)
                        if (d = b[f[g]], e = this.options.columns[d]) this.colgroup[d] && (this.colgroup[d].style.width = c ? parseInt(e.width || this.options.defaultColWidth) + _$_43fc[44] : _$_43fc[144]), e.visible = c;
                    this.resetBorders();
                    C.refresh.call(this)
                },
                a = function(c) {
                    c.getColumn = a[_$_43fc[51]];
                    c.getColumnIdByName = a.getByName;
                    c.getPrimaryKey = a.getPrimaryKey;
                    c.getSelectedColumns = a.selected;
                    c.getOptions = a.getOptions;
                    c.getColumnOptions = a.getOptions;
                    c.moveColumn = a.move;
                    c.insertColumn = a.add;
                    c.deleteColumn = a.del;
                    c.getProperties = a.properties;
                    c.setProperties = a.properties;
                    c.getProperty = a.properties;
                    c.setProperty = a.properties;
                    c.showColumn = k.bind(c, !0);
                    c.hideColumn = k.bind(c, !1)
                };
            a[_$_43fc[51]] = function(c) {
                return this.options.columns[c] || !1
            };
            a.getByName = function(c) {
                for (var b = this.options.columns, d = 0; d < b.length; d++)
                    if (b[d].name == c) return d;
                return !1
            };
            a.getPrimaryKey = function() {
                for (var c = 0; c < this.options.columns.length; c++)
                    if (1 ==
                        this.options.columns[c].primaryKey) return c;
                return !1
            };
            a.getOptions = function(c, b) {
                var d = this.options.columns[c];
                Object.keys(this.options.cells).length && void 0 !== b && (c = A.getColumnNameFromCoords(c, b), this.options.cells[c] && (d = this.options.cells[c]));
                if (!d) d = {
                    type: _$_43fc[56]
                };
                else if (!d.type || typeof d.type == _$_43fc[17] && !t.editors[d.type]) d.type = _$_43fc[56];
                return d
            };
            a.selected = function(c) {
                c = [];
                var b = this.getHighlighted();
                if (b)
                    for (var d = b[0]; d <= b[2]; d++) c.push(d);
                return c
            };
            a.move = function(c, b) {
                if (!N.call(this.parent,
                        this)) return !1;
                c = parseInt(c);
                b = parseInt(b);
                if (!this.options.columns[c]) return console.error(_$_43fc[281]), !1;
                if (!this.options.columns[b]) return console.error(_$_43fc[282]), !1;
                if (0 < Object.keys(this.getMerge()).length) {
                    var d = null;
                    this.merged.cols[c] ? d = !0 : this.merged.cols[b] && (c > b ? this.merged.cols[b - 1] && (d = !0) : this.merged.cols[b + 1] && (d = !0));
                    d && this.destroyMerged()
                }
                this.resetBorders();
                d = this.options.freezeColumns;
                0 < d && (c < d || b < d) ? ia.reset.call(this) : d = null;
                if (this.headers[b] && this.headers[b].parentNode) {
                    this.headers[c] ||
                        ya.create.call(this, c);
                    var e = (c > b ? this.headers[b] : this.headers[b].nextSibling) || null;
                    this.headerContainer.insertBefore(this.headers[c], e);
                    e = (c > b ? this.colgroup[b] : this.colgroup[b].nextSibling) || null;
                    this.colgroupContainer.insertBefore(this.colgroup[c], e);
                    for (var f = 0; f < this.tbody.children.length; f++) {
                        var g = this.tbody.children[f].getAttribute(_$_43fc[43]);
                        e = (c > b ? this.records[g][b].element : this.records[g][b].element.nextSibling) || null;
                        this.tbody.children[f].insertBefore(I[_$_43fc[51]].call(this, c, g),
                            e)
                    }
                    if (this.options.footers)
                        for (f = 0; f < this.tfoot.children.length; f++) e = (c > b ? this.footers.content[f][b].element : this.footers.content[f][b].element.nextSibling) || null, this.tfoot.children[f].insertBefore(W.create.call(this, c, f), e)
                } else if (this.headers[c] && this.headers[c].parentNode) {
                    this.headerContainer.removeChild(this.headers[c]);
                    this.colgroupContainer.removeChild(this.colgroup[c]);
                    for (f = 0; f < this.tbody.children.length; f++) g = parseInt(this.tbody.children[f].getAttribute(_$_43fc[43])), this.tbody.children[f].removeChild(this.records[g][c].element);
                    if (this.options.footers)
                        for (f = 0; f < this.tfoot.children.length; f++) this.tfoot.children[f].removeChild(this.footers.content[f][c].element)
                }
                this.options.columns.splice(b, 0, this.options.columns.splice(c, 1)[0]);
                this.headers.splice(b, 0, this.headers.splice(c, 1)[0]);
                this.colgroup.splice(b, 0, this.colgroup.splice(c, 1)[0]);
                for (f = 0; f < this.rows.length; f++) this.dataType || this.options.data[f].splice(b, 0, this.options.data[f].splice(c, 1)[0]), this.records[f].splice(b, 0, this.records[f].splice(c, 1)[0]);
                if (this.options.footers)
                    for (f =
                        0; f < this.options.footers.length; f++) this.options.footers[f].splice(b, 0, this.options.footers[f].splice(c, 1)[0]), this.footers.content[f].splice(b, 0, this.footers.content[f].splice(c, 1)[0]);
                G.call(this, _$_43fc[68], {
                    f: c,
                    t: b
                });
                T.references.call(this);
                T.update.call(this);
                0 < d && (c <= d || d <= d) && ia[_$_43fc[11]].call(this, d);
                Q.call(this.parent, {
                    worksheet: this,
                    action: _$_43fc[68],
                    oldValue: c,
                    newValue: b
                });
                B.call(this.parent, _$_43fc[283], this, c, b)
            };
            a.add = function(c, b, d, e, f, g) {
                if (!N.call(this.parent, this)) return !1;
                if (!this.options.allowInsertColumn) return console.error(_$_43fc[284]),
                    !1;
                var h = [];
                if (0 < c) var l = parseInt(c);
                else if (l = 1, Array.isArray(c) && !f)
                    if (Array.isArray(c[0])) f = c;
                    else {
                        f = [];
                        for (var n = 0; n < c.length; n++) f[n] = [c[n]]
                    } f || (f = []);
                d = d ? !0 : !1;
                c = this.options.columns.length - 1;
                if (void 0 == b || b > c || 0 > b) b = c, d = !1;
                l || (l = 1);
                if (!1 === B.call(this.parent, _$_43fc[285], this, b, l, d)) return console.log(_$_43fc[286]), !1;
                da.reset.call(this);
                if (1 == this.dataType) var q = parseInt((_$_43fc[3] + Date.now()).substr(-8));
                e || (e = []);
                for (n = 0; n < l; n++) e[n] || (e[n] = {
                    type: _$_43fc[56],
                    source: [],
                    options: [],
                    width: this.options.defaultColWidth,
                    align: this.options.defaultColAlign
                }), 1 == this.dataType && typeof e[n].name == _$_43fc[13] && (e[n].name = _$_43fc[287] + (q + n));
                var p = d ? b : b + 1;
                this.options.columns = A.injectArray(this.options.columns, p, e);
                q = function(u, x) {
                    for (var y = p; y < l + p; y++) ya.create.call(this, y), u ? (this.headerContainer.insertBefore(this.headers[y], u), this.colgroupContainer.insertBefore(this.colgroup[y], x)) : (this.headerContainer.appendChild(this.headers[y]), this.colgroupContainer.appendChild(this.colgroup[y]))
                };
                n = function(u, x) {
                    if (x)
                        for (var y =
                                p; y < l + p; y++) this.rows[u].element.insertBefore(I[_$_43fc[51]].call(this, y, u), x);
                    else
                        for (y = p; y < l + p; y++) this.rows[u].element.appendChild(I[_$_43fc[51]].call(this, y, u))
                };
                var r = this.headers.splice(p),
                    w = this.colgroup.splice(p);
                r[0] ? 0 <= Array.prototype.indexOf.call(this.headerContainer.children, r[0]) && q.call(this, r[0], w[0]) : this.headers[c] && 0 <= Array.prototype.indexOf.call(this.headerContainer.children, this.headers[c]) && q.call(this);
                this.headers = this.headers.concat(r);
                this.colgroup = this.colgroup.concat(w);
                W.adjust.call(this, p, l, 1);
                la.adjust.call(this, b, l, 1, g);
                g = 0;
                w = r = q = null;
                for (var m = 0; m < this.options.data.length; m++) {
                    this.dataType || (r = this.options.data[m].splice(p));
                    w = this.records[m].splice(p);
                    g = 0;
                    for (var v = p; v < l + p; v++) q = f[m] && f[m][g] ? f[m][g] : _$_43fc[3], K.call(this, v, m, q), h.push({
                        x: v,
                        y: m,
                        value: q
                    }), ka.cell.call(this, v, m), g++;
                    this.rows[m].element && this.rows[m].element.parentNode && (w[0] ? 0 <= Array.prototype.indexOf.call(this.rows[m].element.children, w[0].element) && n.call(this, m, w[0].element) : this.records[m][c].element &&
                        0 <= Array.prototype.indexOf.call(this.rows[m].element.children, this.records[m][c].element) && n.call(this, m));
                    this.dataType || (this.options.data[m] = this.options.data[m].concat(r));
                    this.records[m] = this.records[m].concat(w)
                }
                this.merged.cols[p] && X.updateConfig.call(this, 0, 1, p - 1, l);
                G.call(this, _$_43fc[65], {
                    numOfColumns: l,
                    columnNumber: b,
                    insertBefore: d ? 1 : 0,
                    properties: e,
                    data: f
                });
                T.references.call(this);
                h.length && aa.applyValues.call(this, h);
                T.update.call(this);
                this.refreshBorders();
                Q.call(this.parent, {
                    worksheet: this,
                    action: _$_43fc[65],
                    columnNumber: b,
                    numOfColumns: l,
                    insertBefore: d,
                    properties: e,
                    data: f
                });
                B.call(this.parent, _$_43fc[288], this, b, l, [], d)
            };
            a.del = function(c, b) {
                if (!N.call(this.parent, this)) return !1;
                if (!this.options.allowDeleteColumn) return console.error(_$_43fc[289]), !1;
                var d = {},
                    e = [],
                    f = 0;
                if (1 < this.options.columns.length) {
                    void 0 == c && (b = this.getSelectedColumns(!0), b.length ? (c = parseInt(b[0]), b = parseInt(b.length)) : (c = this.options.columns.length - 1, b = 1));
                    var g = this.options.columns.length - 1;
                    if (void 0 == c || c > g ||
                        0 > c) c = g;
                    b || (b = 1);
                    c + b >= this.options.columns.length && (b = this.options.columns.length - c, b >= this.options.columns.length && (b--, console.error(_$_43fc[290])));
                    if (!1 === B.call(this.parent, _$_43fc[291], this, c, b)) return console.log(_$_43fc[292]), !1;
                    if (-1 < parseInt(c) && 0 < parseInt(b)) {
                        this.resetBorders();
                        g = null;
                        var h = [],
                            l = [],
                            n = [];
                        var q = 0;
                        for (var p = c; p < c + b; p++) 0 <= Array.prototype.indexOf.call(this.headerContainer.children, this.headers[p]) && (this.colgroup[p].parentNode.removeChild(this.colgroup[p]), this.headers[p].parentNode.removeChild(this.headers[p]),
                            f++), n[q] = this.options.columns[p], q++;
                        for (var r = 0; r < this.options.data.length; r++)
                            for (p = c; p < c + b; p++) q = A.getColumnNameFromCoords(p, r), I.getAttributes.call(this, _$_43fc[277], q, d), I.getAttributes.call(this, _$_43fc[98], q, d), I.getAttributes.call(this, _$_43fc[278], q, d), I.getAttributes.call(this, _$_43fc[279], q, d), I.getAttributes.call(this, _$_43fc[262], q, d), I.getAttributes.call(this, _$_43fc[261], q, d), e[q] = !0, this.records[r][p].element && 0 <= Array.prototype.indexOf.call(this.rows[r].element.children, this.records[r][p].element) &&
                                this.records[r][p].element.parentNode.removeChild(this.records[r][p].element);
                        if (p = S.getChain.call(this, d.values)) d.formulas = p;
                        this.options.columns.splice(c, b);
                        this.headers.splice(c, b);
                        this.colgroup.splice(c, b);
                        for (r = 0; r < this.options.data.length; r++) this.dataType || (l[r] = this.options.data[r].splice(c, b)), h[r] = this.records[r].splice(c, b);
                        W.adjust.call(this, c, b, 0);
                        (p = la.adjust.call(this, c, b, 0)) && (g = {
                            nested: p
                        });
                        X.updateConfig.call(this, 0, 0, c, b);
                        G.call(this, _$_43fc[66], {
                            columnNumber: c,
                            numOfColumns: b
                        });
                        T.references.call(this, e);
                        0 < f && (1 == this.thead.children.length ? C[_$_43fc[46]].call(this, null, c) : C.refresh.call(this));
                        T.update.call(this, !0);
                        this.refreshBorders();
                        Q.call(this.parent, {
                            worksheet: this,
                            action: _$_43fc[66],
                            columnNumber: c,
                            numOfColumns: b,
                            insertBefore: 1,
                            data: l,
                            properties: n,
                            attributes: d,
                            extra: g
                        });
                        B.call(this.parent, _$_43fc[293], this, c, b, h, n, d)
                    }
                }
            };
            a.properties = function(c, b) {
                if (this.options.columns[c]) {
                    if (b) {
                        if (!N.call(this.parent, this)) return !1;
                        b = typeof b == _$_43fc[17] ? {
                            type: b
                        } : b;
                        b.type && t.editors[b.type] ||
                            (b.type = _$_43fc[56]);
                        parseInt(b.width) || (b.width = this.options.defaultColWidth || 100);
                        this.edition && this.closeEditor(this.edition, !1);
                        var d = this.options.columns[c],
                            e = t.editors[d.type],
                            f = t.editors[b.type];
                        if (e && typeof e.destroyCell == _$_43fc[85])
                            for (var g = 0; g < this.records.length; g++) this.records[g][c].element && e.destroyCell(this.records[g][c].element);
                        this.options.columns[c] = b;
                        if (f && typeof f.createCell == _$_43fc[85])
                            for (g = 0; g < this.records.length; g++) this.records[g] && this.records[g][c] && this.records[g][c].element &&
                                (I.applyProperties.call(this, this.records[g][c].element, c, g), f.createCell(this.records[g][c].element, K.call(this, c, g), c, g, this, b));
                        b.width && this.colgroup[c] && this.colgroup[c].setAttribute(_$_43fc[152], parseInt(b.width));
                        b.title && (this.headers[c] && (this.headers[c].innerText = b.title, this.headers[c].setAttribute(_$_43fc[166], b.title)), this.options.columns[c].title = b.title);
                        b.tooltip && (this.headers[c] && (this.headers[c].title = b.tooltip), this.options.columns[c].tooltip = b.tooltip);
                        this.refreshBorders();
                        Q.call(this.parent, {
                            worksheet: this,
                            action: _$_43fc[77],
                            column: c,
                            newValue: b,
                            oldValue: d
                        });
                        G.call(this, _$_43fc[77], {
                            column: c,
                            options: b
                        });
                        return !0
                    }
                    return this.options.columns[c]
                }
                console.error(_$_43fc[294])
            };
            return a
        }(),
        Ma = function() {
            var k = {},
                a = {},
                c = function(d) {
                    if (typeof d == _$_43fc[17]) {
                        var e = d;
                        d = A.getCoordsFromColumnName(d);
                        var f = d[0];
                        d = d[1]
                    } else f = d.x, d = d.y, e = A.getColumnNameFromCoords(f, d);
                    if (this.records[d] && this.records[d][f] && this.records[d][f].element) {
                        a[e] = this.options.style[e];
                        this.records[d][f].element.setAttribute(_$_43fc[98],
                            _$_43fc[3]);
                        var g = this.options.columns[f].align ? this.options.columns[f].align : this.options.defaultColAlign;
                        g && (this.records[d][f].element.style.textAlign = g)
                    }
                    this.options.style && this.options.style[e] && delete this.options.style[e]
                },
                b = function(d) {
                    d.getStyle = b[_$_43fc[51]];
                    d.setStyle = b[_$_43fc[11]];
                    d.resetStyle = b.reset
                };
            b[_$_43fc[51]] = function(d) {
                var e = this.options.style;
                if (!e) return !1;
                if (d) {
                    if (typeof d == _$_43fc[17]) return e[d];
                    var f = [],
                        g = Object.keys(d);
                    if (0 < g.length)
                        for (var h = 0; h < g.length; h++) {
                            if (typeof d[h] ==
                                _$_43fc[17]) var l = e && e[d[h]] ? e[d[h]] : _$_43fc[3];
                            else l = A.getColumnNameFromCoords(d[h].x, d[h].y), l = e && e[l] ? e[l] : _$_43fc[3];
                            f.push(l)
                        }
                    return f
                }
                return this.options.style
            };
            b[_$_43fc[11]] = function(d, e, f, g) {
                if (!N.call(this.parent, this)) return !1;
                this.options.style || (this.options.style = {});
                var h = this.options.style,
                    l = null,
                    n = [],
                    q = [],
                    p = {};
                q = function(m, v, u) {
                    if (!p[m] && (h[m] ? n = h[m].split(_$_43fc[263]) : (h[m] = _$_43fc[3], n = []), p[m] = {}, n.length))
                        for (var x = 0; x < n.length; x++) n[x] && n[x].trim() && (l = n[x].split(_$_43fc[136]),
                            p[m][l[0].trim()] = l[1].trim());
                    a[m] || (a[m] = []);
                    k[m] || (k[m] = []);
                    p[m][v] || (p[m][v] = _$_43fc[3]);
                    a[m].push([v + _$_43fc[136] + p[m][v]]);
                    p[m][v] = p[m][v] && p[m][v] == u && !g ? _$_43fc[3] : u;
                    k[m].push([v + _$_43fc[136] + p[m][v]]);
                    u = A.getCoordsFromColumnName(m);
                    this.records[u[1]] && this.records[u[1]][u[0]] && this.records[u[1]][u[0]].element && (this.records[u[1]][u[0]].element.style[v] = p[m][v]);
                    p[m][v] || delete p[m][v]
                };
                k = {};
                a = {};
                if (e)
                    if (typeof d == _$_43fc[17]) q.call(this, d, e, f);
                    else {
                        if (d && d.length)
                            for (var r = 0; r < d.length; r++) {
                                var w =
                                    A.getColumnNameFromCoords(d[r].x, d[r].y);
                                q.call(this, w, e, f)
                            }
                    }
                else
                    for (f = Object.keys(d), r = 0; r < f.length; r++)
                        if (w = d[f[r]])
                            for (typeof w == _$_43fc[17] && (w = w.split(_$_43fc[263])), e = 0; e < w.length; e++) w[e] && (typeof w[e] == _$_43fc[17] && (w[e] = w[e].split(_$_43fc[136])), w[e][0].trim() && q.call(this, f[r], w[e][0].trim(), w[e][1].trim()));
                n = {};
                q = Object.keys(p);
                if (q.length) {
                    for (r = 0; r < q.length; r++) {
                        n[q[r]] || (n[q[r]] = []);
                        d = Object.keys(p[q[r]]);
                        for (e = 0; e < d.length; e++) n[q[r]].push(d[e] + _$_43fc[114] + p[q[r]][d[e]]);
                        this.options.style[q[r]] =
                            n[q[r]].join(_$_43fc[295])
                    }
                    q = Object.keys(k);
                    for (r = 0; r < q.length; r++) k[q[r]] = k[q[r]].join(_$_43fc[263]);
                    q = Object.keys(a);
                    for (r = 0; r < q.length; r++) a[q[r]] = a[q[r]].join(_$_43fc[263]);
                    this.refreshBorders();
                    Q.call(this.parent, {
                        worksheet: this,
                        action: _$_43fc[71],
                        oldValue: a,
                        newValue: k
                    });
                    G.call(this, _$_43fc[71], [k]);
                    B.call(this.parent, _$_43fc[296], this, k, a)
                }
            };
            b.reset = function(d) {
                if (!N.call(this.parent, this)) return !1;
                if (d) {
                    a = {};
                    if (Array.isArray(d))
                        for (var e = 0; e < d.length; e++) c.call(this, d[e]);
                    else c.call(this,
                        d);
                    this.refreshBorders();
                    Q.call(this.parent, {
                        worksheet: this,
                        action: _$_43fc[72],
                        cells: d,
                        oldValue: a
                    });
                    G.call(this, _$_43fc[72], [d]);
                    B.call(this.parent, _$_43fc[298], this, d)
                } else console.error(_$_43fc[297])
            };
            b.update = function(d) {
                k = {};
                a = {};
                for (var e = Object.keys(d), f = 0; f < e.length; f++) {
                    a[e[f]] = this.options.style[e[f]] || _$_43fc[3];
                    k[e[f]] = this.options.style[e[f]] = d[e[f]];
                    var g = A.getCoordsFromColumnName(e[f]),
                        h = g[0];
                    g = g[1];
                    this.records[g] && this.records[g][h] && this.records[g][h].element && (this.records[g][h].element.setAttribute(_$_43fc[98],
                        d[e[f]]), this.records[g][h].element.style.textAlign || (this.records[g][h].element.style.textAlign = this.options.columns[h].align || this.options.defaultColAlign || _$_43fc[53]))
                }
                G.call(this, _$_43fc[72], [e]);
                G.call(this, _$_43fc[71], [k]);
                B.call(this.parent, _$_43fc[296], this, k, a)
            };
            return b
        }(),
        Sa = function() {
            var k = function(a) {
                a.getComments = k[_$_43fc[51]];
                a.setComments = k[_$_43fc[11]]
            };
            k[_$_43fc[51]] = function(a) {
                return a && typeof a == _$_43fc[17] ? this.options.comments && this.options.comments[a] ? this.options.comments[a] :
                    !1 : this.options.comments
            };
            k[_$_43fc[11]] = function(a, c) {
                if (!N.call(this.parent, this)) return !1;
                if (typeof a == _$_43fc[17]) {
                    var b = {};
                    b[a] = c
                } else b = a;
                if (c = B.call(this.parent, _$_43fc[299], this, b)) b = c;
                else if (!1 === c) return !1;
                this.options.comments || (this.options.comments = {});
                a = {};
                var d = Object.keys(b);
                if (d.length) {
                    for (var e = 0; e < d.length; e++) {
                        var f = A.getCoordsFromColumnName(d[e]);
                        this.records[f[1]] && this.records[f[1]][f[0]] && (a[d[e]] = this.options.comments[d[e]] || _$_43fc[3], (c = b[d[e]]) ? this.options.comments[d[e]] =
                            c : delete this.options.comments[d[e]], this.records[f[1]][f[0]].element && (c ? this.records[f[1]][f[0]].element.setAttribute(_$_43fc[264], c) : this.records[f[1]][f[0]].element.removeAttribute(_$_43fc[264])))
                    }
                    Q.call(this.parent, {
                        worksheet: this,
                        action: _$_43fc[76],
                        newValue: b,
                        oldValue: a
                    });
                    G.call(this, _$_43fc[76], [b]);
                    B.call(this.parent, _$_43fc[300], this, b, a)
                }
            };
            return k
        }(),
        Ta = function() {
            var k = function(a) {
                a.getMeta = k[_$_43fc[51]];
                a.setMeta = k[_$_43fc[11]];
                a.resetMeta = k.reset
            };
            k[_$_43fc[51]] = function(a, c) {
                if (a) {
                    if (this.options.meta[a]) return c ?
                        this.options.meta[a][c] : this.options.meta[a]
                } else return this.options.meta
            };
            k[_$_43fc[11]] = function(a, c, b) {
                if (!N.call(this.parent, this)) return !1;
                this.options.meta || (this.options.meta = {});
                if (typeof a == _$_43fc[17] && c) {
                    b || (b = _$_43fc[3]);
                    var d = {};
                    d[a] = {};
                    d[a][c] = b;
                    a = d
                }
                c = Object.keys(a);
                if (c.length) {
                    b = {};
                    for (d = 0; d < c.length; d++) {
                        this.options.meta[c[d]] || (this.options.meta[c[d]] = {});
                        b[c[d]] || (b[c[d]] = {});
                        for (var e = Object.keys(a[c[d]]), f = 0; f < e.length; f++) this.options.meta[c[d]][e[f]] = a[c[d]][e[f]], b[c[d]][e[f]] =
                            a[c[d]][e[f]]
                    }
                    Object.keys(b).length && (G.call(this, _$_43fc[301], [b]), B.call(this.parent, _$_43fc[302], this, b))
                }
            };
            k.reset = function() {
                this.options.meta = {};
                G.call(this, _$_43fc[303], {});
                B.call(this.parent, _$_43fc[304], this, {})
            };
            return k
        }(),
        Ua = function() {
            var k = function(a) {
                a.orderBy = k.execute
            };
            k.handler = function(a, c) {
                return function(b, d) {
                    b = b[1];
                    d = d[1];
                    return a ? b === _$_43fc[3] && d !== _$_43fc[3] ? 1 : b !== _$_43fc[3] && d === _$_43fc[3] ? -1 : b > d ? -1 : b < d ? 1 : 0 : b === _$_43fc[3] && d !== _$_43fc[3] ? 1 : b !== _$_43fc[3] && d === _$_43fc[3] ?
                        -1 : b > d ? 1 : b < d ? -1 : 0
                }
            };
            k.execute = function(a, c, b) {
                if (0 <= a) {
                    c = null == c ? this.headers[a].classList.contains(_$_43fc[305]) ? 1 : 0 : c ? 1 : 0;
                    for (var d, e, f = [], g = 0; g < this.options.data.length; g++) d = Da.getOptions.call(this, a, g), e = this.records[g][a].v, e = d.type == _$_43fc[251] || d.type == _$_43fc[256] || d.type == _$_43fc[257] || d.type == _$_43fc[306] || d.type == _$_43fc[14] || d.type == _$_43fc[307] ? Number(e) : jSuites.isNumeric(e) ? Number(e) : e.toLowerCase(), f[g] = [g, e];
                    g = typeof this.parent.config.sorting === _$_43fc[85] ? this.parent.config.sorting(c,
                        a) : k.handler(c, a);
                    f.sort(g);
                    d = b ? !0 : !1;
                    if (!b)
                        for (b = [], g = 0; g < f.length; g++) b[g] = f[g][0];
                    if (g = B.call(this.parent, _$_43fc[308], this, a, c, b)) b = g;
                    else if (!1 === g) return !1;
                    if (d) this.destroyMerged();
                    else if (0 < Object.keys(this.getMerge()).length)
                        if (confirm(z(_$_43fc[309]))) this.destroyMerged();
                        else return !1;
                    if (b.length < f.length)
                        for (g = 0; g < f.length; g++) - 1 == b.indexOf(f[g][0]) && b.push(f[g][0]);
                    f = [];
                    for (g = 0; g < b.length; g++) f[b[g]] = g;
                    Aa.call(this, !1);
                    da.reset.call(this);
                    G.call(this, _$_43fc[78], {
                        column: a,
                        direction: c,
                        order: b
                    });
                    k.update.call(this, b);
                    k.arrow.call(this, a, c);
                    da.refresh.call(this);
                    Q.call(this.parent, {
                        worksheet: this,
                        action: _$_43fc[78],
                        column: a,
                        newValue: b,
                        oldValue: f,
                        direction: c
                    });
                    B.call(this.parent, _$_43fc[310], this, a, c, b);
                    return !0
                }
            };
            k.update = function(a) {
                for (var c = [], b = 0; b < a.length; b++) c[b] = this.options.data[a[b]];
                for (b = 0; b < a.length; b++) this.options.data[b] = c[b];
                c = [];
                for (b = 0; b < a.length; b++) c[b] = this.records[a[b]];
                this.records = c;
                c = [];
                for (b = 0; b < a.length; b++) c[b] = this.rows[a[b]];
                this.rows = c;
                T.references.call(this);
                this.searchInput && this.searchInput.value ? ma.update.call(this, null) : Y.update.call(this)
            };
            k.arrow = function(a, c) {
                for (var b = 0; b < this.headers.length; b++) this.headers[b].classList.remove(_$_43fc[311]), this.headers[b].classList.remove(_$_43fc[305]);
                c ? this.headers[a].classList.add(_$_43fc[311]) : this.headers[a].classList.add(_$_43fc[305])
            };
            return k
        }(),
        qa = function() {
            var k = function(a) {
                a.whichPage = k.whichPage;
                a.quantityOfPages = k.quantityOfPages;
                a.page = k[_$_43fc[11]];
                a.updatePagination = k.update
            };
            k.build = function() {
                this.pageNumber =
                    0;
                var a = document.createElement(_$_43fc[20]),
                    c = document.createElement(_$_43fc[20]);
                this.pagination = document.createElement(_$_43fc[20]);
                this.pagination.classList.add(_$_43fc[312]);
                this.pagination.appendChild(a);
                this.pagination.appendChild(c);
                this.options.pagination || (this.pagination.style.display = _$_43fc[40]);
                this.element.appendChild(this.pagination)
            };
            k.pageUp = function() {
                0 < this.pageNumber && (this.pageNumber--, k[_$_43fc[11]].call(this, this.pageNumber))
            };
            k.pageDown = function() {
                this.pageNumber < k.quantityOfPages.call(this) -
                    1 && (this.pageNumber++, k[_$_43fc[11]].call(this, this.pageNumber))
            };
            k.whichPage = function(a) {
                if (0 < this.options.pagination) return this.rows[a] ? (this.results && (a = this.results.indexOf(parseInt(a))), Math.ceil((parseInt(a) + 1) / parseInt(this.options.pagination)) - 1) : null;
                console.log(_$_43fc[313]);
                return !1
            };
            k.quantityOfPages = function() {
                if (0 < this.options.pagination) return Math.ceil((this.results ? this.results.length : this.rows.length) / parseInt(this.options.pagination));
                console.log(_$_43fc[313]);
                return !1
            };
            k[_$_43fc[11]] =
                function(a, c) {
                    var b = this.pageNumber,
                        d = parseInt(this.options.pagination);
                    if (d) {
                        if (null == a || -1 == a) a = Math.ceil((this.results ? this.results : this.rows).length / d) - 1;
                        if (!1 === B.call(this.parent, _$_43fc[315], this, a, b, d)) return !1;
                        this.pageNumber = a;
                        C.resetY.call(this);
                        W.refresh.call(this);
                        typeof c == _$_43fc[85] && c.call(this);
                        B.call(this.parent, _$_43fc[316], this, a, b, d)
                    } else console.error(_$_43fc[314])
                };
            k.update = function() {
                this.pagination.children[0].innerHTML = _$_43fc[3];
                this.pagination.children[1].innerHTML = _$_43fc[3];
                if (this.options.pagination) {
                    var a = this.results ? this.results.length : this.rows.length;
                    if (a) {
                        a = Math.ceil(a / this.options.pagination);
                        if (6 > this.pageNumber) var c = 1,
                            b = 10 > a ? a : 10;
                        else 5 > a - this.pageNumber ? (c = a - 9, b = a, 1 > c && (c = 1)) : (c = this.pageNumber - 4, b = this.pageNumber + 5);
                        if (1 < c) {
                            var d = document.createElement(_$_43fc[20]);
                            d.className = _$_43fc[318];
                            d.innerHTML = _$_43fc[215];
                            d.title = 1;
                            this.pagination.children[1].appendChild(d)
                        }
                        for (; c <= b; c++) d = document.createElement(_$_43fc[20]), d.className = _$_43fc[318], d.innerHTML =
                            c, this.pagination.children[1].appendChild(d), this.pageNumber == c - 1 && d.classList.add(_$_43fc[319]);
                        b < a && (d = document.createElement(_$_43fc[20]), d.className = _$_43fc[318], d.innerHTML = _$_43fc[216], d.title = a, this.pagination.children[1].appendChild(d));
                        this.pagination.children[0].innerHTML = z(_$_43fc[320], [this.pageNumber + 1, a])
                    } else this.pagination.children[0].innerHTML = z(_$_43fc[317])
                }
            };
            return k
        }(),
        ia = function() {
            var k = function(a) {
                a.setFreezeColumns = k[_$_43fc[11]];
                a.resetFreezeColumns = k.reset
            };
            k.width = function() {
                if (this.options.freezeColumns) {
                    k.headers.call(this);
                    for (var a = 0; a < this.rows.length; a++) this.rows[a].element && k.update.call(this, a)
                }
            };
            k.update = function(a) {
                var c, b;
                if (b = this.options.freezeColumns) {
                    var d = va.call(this) ? 50 : 0;
                    if (this.rows[a].element)
                        for (var e = 0; e < b; e++) {
                            if (c = this.records[a][e].element) c.classList.add(_$_43fc[50]), c.style.left = d + 1 + _$_43fc[44];
                            d += this.options.columns[e].width
                        }
                }
            };
            k.headers = function() {
                var a = va.call(this) ? 50 : 0,
                    c;
                if (c = this.options.freezeColumns)
                    for (var b = 0; b < c; b++) {
                        if (this.headers[b]) {
                            this.headers[b].classList.add(_$_43fc[50]);
                            this.headers[b].style.left = a + 1 + _$_43fc[44];
                            if (this.options.footers)
                                for (var d = 0; d < this.options.footers.length; d++) this.footers.content[d][b].element.classList.add(_$_43fc[50]), this.footers.content[d][b].element.style.left = a + 1 + _$_43fc[44];
                            a += this.options.columns[b].width
                        }
                    } else k.reset.call(this)
            };
            k[_$_43fc[11]] = function(a) {
                k.reset.call(this);
                this.options.freezeColumns = a;
                k.width.call(this);
                C.call(this)
            };
            k.reset = function() {
                C[_$_43fc[46]].call(this, null, this.options.freezeColumns);
                for (var a, c = 1; c <= this.options.freezeColumns; c++) {
                    if (a =
                        this.thead.lastChild) a.children[c].classList.remove(_$_43fc[50]), a.children[c].style.left = _$_43fc[3];
                    for (var b = 0; b < this.rows.length; b++)
                        if (a = this.rows[b].element) a.children[c].classList.remove(_$_43fc[50]), a.children[c].style.left = _$_43fc[3];
                    if (this.options.footers)
                        for (b = 0; b < this.options.footers.length; b++) this.tfoot.children[b].children[c].classList.remove(_$_43fc[50]), this.tfoot.children[b].children[c].style.left = _$_43fc[3]
                }
                this.options.freezeColumns = 0
            };
            k.getWidth = function() {
                var a = 0;
                if (0 < this.options.freezeColumns)
                    for (var c =
                            0; c <= this.options.freezeColumns; c++) a = this.thead.lastChild.children[c] ? a + this.thead.lastChild.children[c].offsetWidth : a + this.options.columns[c].width;
                return a
            };
            return k
        }(),
        T = function() {
            var k = function(b, d) {
                    if (this.options[b]) {
                        for (var e = {}, f = Object.keys(this.options[b]), g = 0; g < f.length; g++) e[d && void 0 != d[f[g]] ? d[f[g]] : f[g]] = this.options[b][f[g]];
                        this.options[b] = e
                    }
                },
                a = function() {
                    if (0 < this.options.minSpareRows) {
                        for (var b = 0, d = this.rows.length - 1; 0 <= d; d--) {
                            for (var e = !1, f = 0; f < this.options.columns.length; f++) K.call(this,
                                f, d) && (e = !0);
                            if (e) break;
                            else b++
                        }
                        0 < this.options.minSpareRows - b && this.insertRow(this.options.minSpareRows - b)
                    }
                    if (0 < this.options.minSpareCols) {
                        b = 0;
                        for (f = this.options.columns.length - 1; 0 <= f; f--) {
                            e = !1;
                            for (d = 0; d < this.rows.length; d++) K.call(this, f, d) && (e = !0);
                            if (e) break;
                            else b++
                        }
                        0 < this.options.minSpareCols - b && this.insertColumn(this.options.minSpareCols - b)
                    }
                },
                c = function() {};
            c.build = function() {
                var b = this;
                this.content = document.createElement(_$_43fc[20]);
                this.content.classList.add(_$_43fc[321]);
                this.content.addEventListener(_$_43fc[322],
                    function(f) {
                        if (C.limited.call(b) && !b.options.pagination) {
                            var g = Math.abs(f.deltaX),
                                h = Math.abs(f.deltaY);
                            f.shiftKey || g > h ? 0 > f.deltaX || 0 > f.deltaY ? C.pageLeft.call(b, 1, null, f) : C.pageRight.call(b, 1, null, f) : 0 > f.deltaY ? C.pageUp.call(b, 1, null, f) : C.pageDown.call(b, 1, null, f)
                        }
                    });
                this.content.addEventListener(_$_43fc[323], function(f) {
                    if (!b.edition) return f.preventDefault(), !1
                });
                this.table = document.createElement(_$_43fc[324]);
                this.thead = document.createElement(_$_43fc[325]);
                this.tbody = document.createElement(_$_43fc[326]);
                this.tfoot = document.createElement(_$_43fc[327]);
                this.headers = [];
                this.colgroup = [];
                this.colgroupContainer = document.createElement(_$_43fc[328]);
                var d = document.createElement(_$_43fc[169]);
                d.setAttribute(_$_43fc[152], _$_43fc[329]);
                this.colgroupContainer.appendChild(d);
                this.headerContainer = document.createElement(_$_43fc[145]);
                d = document.createElement(_$_43fc[147]);
                d.classList.add(_$_43fc[330]);
                this.headerContainer.appendChild(d);
                this.thead.appendChild(this.headerContainer);
                this.table.classList.add(_$_43fc[331]);
                this.table.setAttribute(_$_43fc[332], _$_43fc[97]);
                this.table.setAttribute(_$_43fc[333], _$_43fc[97]);
                this.table.setAttribute(_$_43fc[334], _$_43fc[335]);
                this.table.appendChild(this.colgroupContainer);
                this.table.appendChild(this.thead);
                this.table.appendChild(this.tbody);
                this.table.appendChild(this.tfoot);
                this.parent.config.wordWrap && this.table.classList.add(_$_43fc[336]);
                this.options.textOverflow || this.table.classList.add(_$_43fc[337]);
                !1 === this.options.selectLockedCells && this.table.classList.add(_$_43fc[338]);
                this.corner = document.createElement(_$_43fc[20]);
                this.corner.className = _$_43fc[339];
                this.corner.setAttribute(_$_43fc[334], _$_43fc[340]);
                this.corner.setAttribute(_$_43fc[341], _$_43fc[342]);
                0 == this.options.selectionCopy && (this.corner.style.display = _$_43fc[40]);
                this.scrollX = P.build.call(b, _$_43fc[45]);
                this.scrollY = P.build.call(b, _$_43fc[343]);
                d = document.createElement(_$_43fc[20]);
                d.appendChild(this.content);
                d.appendChild(this.scrollY);
                d.classList.add(_$_43fc[344]);
                var e = document.createElement(_$_43fc[20]);
                e.appendChild(d);
                e.appendChild(this.scrollX);
                e.classList.add(_$_43fc[345]);
                this.content.appendChild(this.table);
                this.content.appendChild(this.corner);
                this.element.appendChild(e);
                1 == this.options.tableOverflow && (this.options.tableHeight || (this.options.tableHeight = 300), this.options.tableWidth || (this.options.tableWidth = document.body.offsetWidth - 8), this.content.style.maxHeight = parseInt(this.options.tableHeight) + _$_43fc[44], this.content.style.maxWidth = parseInt(this.options.tableWidth) + _$_43fc[44], 1 == this.options.tableOverflowResizable &&
                    (this.content.style.resize = _$_43fc[346]));
                this.options.freezeColumns > this.options.columns.length && console.error(_$_43fc[347]);
                ma.build.call(this);
                la.build.call(this);
                qa.build.call(this);
                Ba.build.call(this);
                X.build.call(this);
                W.build.call(this);
                C.call(this);
                Y.onload.call(this);
                a.call(this)
            };
            c.references = function(b) {
                var d = [],
                    e = [],
                    f = null,
                    g = null,
                    h = null,
                    l = null;
                for (f = 0; f < this.options.columns.length; f++)
                    if (g = this.headers[f]) h = g.getAttribute(_$_43fc[42]), h != f && (g.setAttribute(_$_43fc[42], f), g.getAttribute(_$_43fc[166]) ||
                        (g.innerHTML = A.getColumnName(f)));
                for (var n = 0; n < this.rows.length; n++) g = this.rows[n], l = g.y, l != n && (g.y = n, d[l] = n, g.element && (g.element.setAttribute(_$_43fc[43], n), g.element.children[0].setAttribute(_$_43fc[43], n), f = this.options.rows && this.options.rows[l] && this.options.rows[l].title ? this.options.rows[l].title : n + 1, g.element.children[0].innerHTML = f));
                var q = function(r, w) {
                    g = this.records[w][r];
                    h = g.x;
                    l = g.y;
                    h != r && (g.x = r, g.element && g.element.setAttribute(_$_43fc[42], r));
                    l != w && (g.y = w, g.element && g.element.setAttribute(_$_43fc[43],
                        w));
                    if (h != r || l != w) {
                        var m = A.getColumnNameFromCoords(h, l);
                        r = A.getColumnNameFromCoords(r, w);
                        e[m] = r
                    }
                };
                if (b) {
                    n = function(r, w) {
                        r && r[w] && delete r[w]
                    };
                    var p = Object.keys(b);
                    for (f = 0; f < p.length; f++) n(this, p[f]), n(this.options.meta, p[f]), n(this.options.cells, p[f]), n(this.options.style, p[f]), n(this.options.comments, p[f])
                }
                for (n = 0; n < this.rows.length; n++)
                    for (f = 0; f < this.options.columns.length; f++) q.call(this, f, n);
                k.call(this, _$_43fc[348], d);
                k.call(this, _$_43fc[277], e);
                k.call(this, _$_43fc[278], e);
                k.call(this, _$_43fc[98],
                    e);
                k.call(this, _$_43fc[279], e);
                k.call(this, _$_43fc[262], e);
                this.options.mergeCells && X.build.call(this);
                S.updateAll.call(this, e, b);
                return e
            };
            c.update = function(b) {
                b && a.call(this);
                if (typeof this.parent.config.updateTable == _$_43fc[85])
                    for (var d = 0; d < this.rows.length; d++)
                        for (var e = 0; e < this.options.columns.length; e++) b = this.records[d][e], this.parent.config.updateTable.call(this, this, b.element, e, d, K.call(this, e, d), b.r ? b.r : b.v);
                P.call(this);
                W.refresh.call(this)
            };
            return c
        }(),
        ka = function() {
            var k = {
                spreadsheet: function(a,
                    c) {
                    var b = {
                        name: null,
                        config: {},
                        el: a,
                        element: a,
                        plugins: [],
                        worksheets: [],
                        history: [],
                        historyIndex: -1,
                        queue: [],
                        ignoreEvents: !1,
                        ignoreHistory: !1,
                        ignorePersistence: !1
                    };
                    za.spreadsheet.call(b, c);
                    b.config.license && (t.license = b.config.license);
                    a.spreadsheet = b;
                    a.jspreadsheet = a.jexcel = b.worksheets;
                    t.spreadsheet.push(b);
                    b.name || (b.name = jSuites.guid());
                    k.bind(b.config.root ? b.config.root : document);
                    var d = b.config.tabs;
                    typeof d !== _$_43fc[84] && (d = {
                        allowCreate: d ? !0 : !1,
                        hideHeaders: d ? !1 : !0,
                        allowChangePosition: b.config.allowMoveWorksheet ?
                            !0 : !1
                    });
                    d.maxWidth || (d.maxWidth = a.offsetWidth - 50 + _$_43fc[44]);
                    d.animation = void 0 == d.animation ? !0 : d.animation;
                    d.onbeforecreate = function() {
                        b.createWorksheet();
                        return !1
                    };
                    d.onclick = function(f, g, h, l, n) {
                        0 <= h && b.openWorksheet(h)
                    };
                    d.onchangeposition = function(f, g, h) {
                        b.updateWorksheet(g, h)
                    };
                    jSuites.tabs(a, d);
                    a.classList.add(_$_43fc[349]);
                    1 == c.fullscreen && a.classList.add(_$_43fc[15]);
                    var e = document.createElement(_$_43fc[20]);
                    b.toolbar = document.createElement(_$_43fc[20]);
                    b.toolbar.className = _$_43fc[350];
                    e.appendChild(b.toolbar);
                    b.filter = Y.build.call(b);
                    e.appendChild(b.filter);
                    b.helper = document.createElement(_$_43fc[20]);
                    b.helper.className = _$_43fc[351];
                    e.appendChild(b.helper);
                    b.loading = document.createElement(_$_43fc[20]);
                    b.loading.classList.add(_$_43fc[352]);
                    e.appendChild(b.loading);
                    e.appendChild(Z.build(b));
                    e.appendChild(Na(b));
                    b.textarea = document.createElement(_$_43fc[353]);
                    b.textarea.className = _$_43fc[354];
                    b.textarea.tabIndex = _$_43fc[355];
                    e.appendChild(b.textarea);
                    d.position === _$_43fc[356] ? a.insertBefore(e, a.children[0]) :
                        a.insertBefore(e, a.children[1]);
                    a.appendChild(Fa.call(b, t.license));
                    b.createWorksheet = ea.createWorksheet;
                    b.deleteWorksheet = ea.deleteWorksheet;
                    b.renameWorksheet = ea.renameWorksheet;
                    b.updateWorksheet = ea.updateWorksheet;
                    b.openWorksheet = ea.openWorksheet;
                    b.moveWorksheet = ea.moveWorksheet;
                    b.getWorksheet = ea.getWorksheet;
                    b.getWorksheetActive = ea.getWorksheetActive;
                    b.getWorksheetInstance = ea.getWorksheetInstance;
                    b.getConfig = function() {
                        var f = b.config;
                        f.worksheets = [];
                        for (var g = 0; g < b.worksheets.length; g++) f.worksheets.push(b.worksheets[g].getConfig());
                        return f
                    };
                    b.fullscreen = Ga;
                    b.progress = Ha;
                    b.undo = Q.undo;
                    b.redo = Q.redo;
                    b.setToolbar = ca[_$_43fc[11]];
                    b.getToolbar = ca[_$_43fc[51]];
                    b.showToolbar = ca.show;
                    b.hideToolbar = ca.hide;
                    b.tools = e;
                    1 !== b.edition && (a = Object.keys(b.config.definedNames)) && a.length && (b.config.definedNames = {}, console.log(_$_43fc[357]));
                    c.plugins && ja.call(b, c.plugins);
                    b.setPlugins = function(f) {
                        ja.call(b, f)
                    };
                    a = Object.keys(t.extensions);
                    if (a.length)
                        for (d = 0; d < a.length; d++)
                            if (typeof t.extensions[a[d]].oninit == _$_43fc[85]) t.extensions[a[d]].oninit(b,
                                c);
                    return b
                },
                worksheet: function(a, c) {
                    var b = {};
                    a.worksheets.push(b);
                    b.parent = a;
                    za(b);
                    Oa(b);
                    ua(b);
                    da(b);
                    Z(b);
                    Ba(b);
                    Qa(b);
                    X(b);
                    Ma(b);
                    Ra(b);
                    aa(b);
                    S(b);
                    I(b);
                    wa(b);
                    Da(b);
                    Ta(b);
                    Sa(b);
                    ya(b);
                    W(b);
                    la(b);
                    ma(b);
                    Y(b);
                    ca(b);
                    pa(b);
                    Va(b);
                    Wa(b);
                    qa(b);
                    ea(b);
                    ia(b);
                    Pa(b);
                    Ua(b);
                    Ea(b);
                    za.worksheet.call(b, c);
                    !t.license && b.options.license && (t.license = b.options.license);
                    a.element.jexcel = a.worksheets ? a.worksheets : b;
                    ja.execute.call(b.parent, _$_43fc[358], [b]);
                    b.onload = function() {
                        this.options.worksheetName || (this.options.worksheetName =
                            _$_43fc[359] + ea.nextName());
                        var d = this.options.worksheetName;
                        D(d) && console.log(_$_43fc[360] + d + _$_43fc[361]);
                        D(d, b);
                        this.element = a.element.tabs.appendElement(this.options.worksheetName, function(e, f) {
                            b.options.worksheetState === _$_43fc[49] && (f.style.display = _$_43fc[40])
                        });
                        this.element.classList.add(_$_43fc[362]);
                        this.element.jexcel = this;
                        T.build.call(this);
                        ja.execute.call(this.parent, _$_43fc[363], [this]);
                        this.options.data && 500 < this.options.data.length && !C.limited.call(this) && !this.options.pagination &&
                            console.error(_$_43fc[364])
                    };
                    return b
                },
                row: function(a, c) {
                    this.rows[a] || (this.rows[a] = {
                        element: null,
                        y: a
                    });
                    c && (this.rows[a].id = c, pa[_$_43fc[11]].call(this, c));
                    this.options.rows && this.options.rows[a] && this.options.rows[a].height && (this.rows[a].height = this.options.rows[a].height);
                    this.records[a] || (this.records[a] = []);
                    this.options.data[a] || (this.options.data[a] = this.dataType ? {} : []);
                    for (c = 0; c < this.options.columns.length; c++) k.cell.call(this, c, a)
                },
                cell: function(a, c, b) {
                    typeof b == _$_43fc[13] && (b = K.call(this,
                        a, c), null === b || void 0 === b) && (b = _$_43fc[3], K.call(this, a, c, b));
                    this.records[c][a] || (this.records[c][a] = {
                        element: null,
                        x: a,
                        y: c,
                        v: b
                    })
                },
                bind: function(a) {
                    for (var c = Object.keys(xa), b = 0; b < c.length; b++)(c[b] == _$_43fc[365] ? window : a).addEventListener(c[b], xa[c[b]], c[b] == _$_43fc[366] || c[b] == _$_43fc[367] || c[b] == _$_43fc[368] ? {
                        passive: !1
                    } : {})
                },
                unbind: function(a) {
                    for (var c = Object.keys(xa), b = 0; b < c.length; b++)(c[b] == _$_43fc[365] ? window : a).removeEventListener(c[b], xa[c[b]])
                }
            };
            return k
        }(),
        Va = function() {
            var k = function(a) {
                a.getWidth =
                    k[_$_43fc[51]];
                a.setWidth = k[_$_43fc[11]]
            };
            k[_$_43fc[51]] = function(a) {
                var c = this.options.columns;
                if (typeof a === _$_43fc[13]) {
                    a = [];
                    for (var b = 0; b < c.length; b++) a.push(c[b].width)
                } else a = c[a].width;
                return a
            };
            k[_$_43fc[11]] = function(a, c, b) {
                if (!N.call(this.parent, this)) return !1;
                if (c) {
                    if (Array.isArray(a)) {
                        b || (b = []);
                        for (var d = 0; d < a.length; d++) {
                            b[d] || (b[d] = this.colgroup[a[d]].getAttribute(_$_43fc[152]));
                            var e = Array.isArray(c) && c[d] ? c[d] : c;
                            this.colgroup[a[d]].setAttribute(_$_43fc[152], e);
                            this.options.columns[a[d]].width =
                                e
                        }
                    } else b || (b = this.colgroup[a].getAttribute(_$_43fc[152])), this.colgroup[a].setAttribute(_$_43fc[152], c), this.options.columns[a].width = c;
                    this.refreshBorders();
                    Q.call(this.parent, {
                        worksheet: this,
                        action: _$_43fc[73],
                        column: a,
                        oldValue: b,
                        newValue: c
                    });
                    G.call(this, _$_43fc[73], {
                        column: a,
                        width: c,
                        oldWidth: b
                    });
                    B.call(this.parent, _$_43fc[369], this, a, c, b)
                }
                C.refresh.call(this)
            };
            return k
        }(),
        Wa = function() {
            var k = function(c) {
                c.getHeight = k[_$_43fc[51]];
                c.setHeight = k[_$_43fc[11]]
            };
            k[_$_43fc[51]] = function(c) {
                if (c) c = this.rows[c].element.style.height;
                else {
                    c = [];
                    for (var b = 0; b < this.rows.length; b++) {
                        var d = this.rows[b].element.style.height;
                        d && (c[b] = d)
                    }
                }
                return c
            };
            var a = function(c, b) {
                b = parseInt(b);
                var d = this.rows[c].height ? this.rows[c].height : this.rows[c].element.offsetHeight || this.options.defaultRowHeight;
                this.rows[c].element.style.height = b + _$_43fc[44];
                this.options.rows[c] || (this.options.rows[c] = {});
                this.options.rows[c].height = b + _$_43fc[44];
                this.rows[c].height = b;
                return d
            };
            k[_$_43fc[11]] = function(c, b, d) {
                if (!N.call(this.parent, this)) return !1;
                var e, f =
                    typeof d == _$_43fc[13] ? !0 : !1;
                if (b) {
                    if (Array.isArray(c))
                        for (!0 === f && (d = []), e = 0; e < c.length; e++) {
                            var g = Array.isArray(b) && b[e] ? b[e] : b;
                            a.call(this, c[e], g);
                            1 == f && d.push(f)
                        } else e = a.call(this, c, b), 1 == f && (d = e);
                    this.refreshBorders();
                    Q.call(this.parent, {
                        worksheet: this,
                        action: _$_43fc[74],
                        row: c,
                        oldValue: d,
                        newValue: b
                    });
                    G.call(this, _$_43fc[74], {
                        row: c,
                        height: b,
                        oldHeight: d
                    });
                    B.call(this.parent, _$_43fc[370], this, c, b, d)
                }
                C.refresh.call(this)
            };
            return k
        }(),
        Y = function() {
            var k = function(g) {
                    var h = e[g];
                    g = b[e[g]];
                    if (!g.element) {
                        g.element =
                            document.createElement(_$_43fc[371]);
                        g.element.innerHTML = g.value;
                        var l = document.createElement(_$_43fc[220]);
                        l.type = _$_43fc[250];
                        l.value = h;
                        l.o = g;
                        g.element.insertBefore(l, g.element.firstChild)
                    }
                    g.element.firstChild.checked = g.selected;
                    return g.element
                },
                a = null,
                c = null,
                b = [],
                d = 0,
                e = null,
                f = function(g) {
                    g.setFilter = f[_$_43fc[11]];
                    g.getFilter = f[_$_43fc[51]];
                    g.openFilter = f.open;
                    g.closeFilter = f.close;
                    g.resetFilters = f.reset;
                    g.showFilter = f.show;
                    g.hideFilter = f.hide
                };
            f.isVisible = function() {
                return c
            };
            f[_$_43fc[11]] =
                function(g, h) {
                    var l = this.headers[g];
                    g = this.options.columns[g];
                    Array.isArray(h) && 0 < h.length ? (l && l.classList.add(_$_43fc[372]), g.filters = h) : (l && l.classList.remove(_$_43fc[372]), g.filters = null);
                    f.update.call(this)
                };
            f[_$_43fc[51]] = function(g) {
                if (g) return this.options.columns[g].filters || null;
                g = {};
                for (var h = 0; h < this.options.columns.length; h++) g[h] = this.options.columns[h].filters || null;
                return g
            };
            f.open = function(g) {
                a = parseInt(g);
                if (this.headers[a].classList.contains(_$_43fc[168])) {
                    c = !0;
                    var h = this.parent.element.getBoundingClientRect(),
                        l = this.headers[a].getBoundingClientRect();
                    g = l.left - h.left;
                    l = l.top - h.top + l.height;
                    h = this.parent.filter;
                    h.style.display = _$_43fc[16];
                    h.style.top = l + _$_43fc[44];
                    h.style.left = g + _$_43fc[44];
                    h.children[0].focus();
                    g = h.children[1].selectAll;
                    h.children[1].textContent = _$_43fc[3];
                    h.children[1].appendChild(g);
                    this.options.columns[a].filters || (g.children[0].checked = !0);
                    g = ma.execute.call(this, null, a);
                    g = A.invert(g);
                    h = [];
                    for (l = 0; l < this.rows.length; l++)
                        if (g[l]) {
                            var n = _$_43fc[3] + K.call(this, a, l),
                                q = _$_43fc[3] + aa.processed.call(this,
                                    a, l, !0);
                            n.substr(0, 1) == _$_43fc[12] && (n = q);
                            h.push([n, q])
                        } h.sort(function(p, r) {
                        return p[0] > r[0] ? 1 : p[0] < r[0] ? -1 : 0
                    });
                    b = [];
                    for (l = 0; l < h.length; l++) b[h[l][0]] = {
                        value: h[l][1],
                        element: null
                    };
                    void 0 !== b[_$_43fc[3]] && (delete b[_$_43fc[3]], b[_$_43fc[3]] = {
                        value: _$_43fc[212] + z(_$_43fc[374]) + _$_43fc[232],
                        element: null
                    });
                    f.search.call(this, _$_43fc[3])
                } else console.log(_$_43fc[373])
            };
            f.close = function(g) {
                if (g) {
                    g = this.parent.filter.children[1];
                    if (1 == g.selectAll.firstChild.checked && e.length == Object.keys(b).length) g = null;
                    else {
                        g = 1 == g.currentSelection.firstChild.checked ? this.options.columns[a].filters || [] : [];
                        for (var h = 0; h < e.length; h++) b[e[h]].selected && g.push(e[h])
                    }
                    f[_$_43fc[11]].call(this, a, g)
                }
                this.parent.filter.style.display = _$_43fc[3];
                this.parent.filter.children[0].value = _$_43fc[3];
                a = null;
                c = !1;
                b = null;
                d = 0;
                e = null
            };
            f.reset = function(g) {
                f.updateDOM.call(this, g, !1);
                sa.reset.call(this);
                f.update.call(this)
            };
            f.execute = function(g) {
                for (var h = this.options.columns, l = [], n = 0; n < this.options.data.length; n++) {
                    for (var q = !0, p = 0; p < h.length; p++)
                        if (h[p].filters &&
                            g !== p) {
                            var r = _$_43fc[3] + K.call(this, p, n),
                                w = _$_43fc[3] + this.getLabelFromCoords(p, n, !0); - 1 == h[p].filters.indexOf(r) && -1 == h[p].filters.indexOf(w) && (q = !1)
                        } q && l.push(n)
                }
                return l
            };
            f.update = function() {
                sa.reset.call(this);
                this.resetSelection();
                this.resetBorders();
                var g = ma.execute.call(this, null);
                if (typeof this.parent.config.onbeforefilter == _$_43fc[85]) {
                    var h = f[_$_43fc[51]].call(this),
                        l = B.call(this.parent, _$_43fc[375], this, h, g);
                    if (l) g = l;
                    else if (!1 === l) return !1
                }
                this.pageNumber = 0;
                g && g.length < this.rows.length &&
                    sa[_$_43fc[11]].call(this, g);
                C.resetY.call(this);
                W.refresh.call(this);
                B.call(this.parent, _$_43fc[376], this, h, g)
            };
            f.show = function(g) {
                f.updateDOM.call(this, g, !0)
            };
            f.hide = function(g) {
                f.updateDOM.call(this, g, !1, !0);
                f.update.call(this)
            };
            f.search = function(g) {
                for (var h = this.parent.filter.children[1]; h.children[1];) h.removeChild(h.lastChild);
                h.currentSelection.firstChild.checked = !1;
                if (g) {
                    e = [];
                    g = new RegExp(g, _$_43fc[377]);
                    for (var l = Object.keys(b), n = 0; n < l.length; n++)(_$_43fc[3] + l[n]).match(g) || (_$_43fc[3] +
                        b[l[n]]).match(g) ? (e.push(l[n]), b[l[n]].selected = !0) : b[l[n]].selected = !1;
                    h.firstChild.checked = !0;
                    this.options.columns[a].filters && h.appendChild(h.currentSelection)
                } else {
                    e = Object.keys(b);
                    if (g = this.options.columns[a].filters)
                        for (n = 0; n < e.length; n++) b[e[n]].selected = !1, 0 <= g.indexOf(e[n]) && (b[e[n]].selected = !0);
                    else
                        for (n = 0; n < e.length; n++) b[e[n]].selected = !0;
                    h.firstChild.checked = g ? !1 : !0
                }
                for (n = d = 0; n < e.length; n++) 200 > n && (g = d++, h.appendChild(k(g)))
            };
            f.build = function() {
                var g = document.createElement(_$_43fc[20]);
                g.className = _$_43fc[378];
                g.onclick = function(q) {
                    q.target.tagName == _$_43fc[379] && q.target.o && (q.target.o.selected = q.target.checked);
                    q = !0;
                    for (var p = 1; p < this.children.length; p++) this.children[p].children[0].checked || (q = !1);
                    this.children[0].children[0].checked = q
                };
                var h = document.createElement(_$_43fc[220]);
                h.type = _$_43fc[56];
                h.placeholder = z(_$_43fc[380]);
                h.className = _$_43fc[381];
                var l = document.createElement(_$_43fc[220]);
                l.type = _$_43fc[382];
                l.value = _$_43fc[383];
                l.className = _$_43fc[384];
                var n = document.createElement(_$_43fc[20]);
                n.className = _$_43fc[385];
                n.appendChild(h);
                n.appendChild(g);
                n.appendChild(l);
                g.currentSelection = document.createElement(_$_43fc[371]);
                g.currentSelection.innerHTML = _$_43fc[386] + z(_$_43fc[387]);
                g.selectAll = document.createElement(_$_43fc[371]);
                g.selectAll.innerHTML = _$_43fc[388] + z(_$_43fc[389]) + _$_43fc[232];
                g.selectAll.onclick = function() {
                    for (var q = this.children[0].checked, p = 1; p < this.parentNode.children.length; p++) this.parentNode.children[p].children[0].checked = q;
                    if (1 == q)
                        for (p = 0; p < e.length; p++) b[e[p]].selected = !0;
                    else
                        for (q = Object.keys(b), p = 0; p < q.length; p++) b[q[p]].selected = !1
                };
                jSuites.lazyLoading(g, {
                    loadDown: function(q) {
                        for (q = 0; d < e.length - 1 && 10 > q;) {
                            var p = d++;
                            g.appendChild(k(p));
                            q++
                        }
                    }
                });
                return n
            };
            f.updateDOM = function(g, h, l) {
                var n = this.headers,
                    q = function(p) {
                        1 == h ? n[p].classList.add(_$_43fc[168]) : (this.options.columns[p].filters = null, n[p].classList.remove(_$_43fc[372]), 1 == l && n[p].classList.remove(_$_43fc[168]))
                    };
                if (g) q.call(this, g);
                else
                    for (g = 0; g < n.length; g++) q.call(this, g)
            };
            f.onload = function() {
                var g, h = !1;
                if (g = this.options.columns)
                    for (var l = 0; l < g.length; l++) Array.isArray(g[l].filters) && 0 < g[l].filters.length && this.headers[l] && (this.headers[l].classList.add(_$_43fc[372]), h = !0);
                1 == h && f.update.call(this)
            };
            return f
        }(),
        sa = function() {
            var k = {};
            k[_$_43fc[11]] = function(a) {
                for (var c = 0; c < a.length; c++) this.rows[a[c]].results = !0;
                this.results = a
            };
            k.reset = function() {
                for (var a = 0; a < this.rows.length; a++) !0 === this.rows[a].results && delete this.rows[a].results;
                this.results = null
            };
            k.refresh = function() {
                for (var a = [], c = 0; c < this.rows.length; c++) !0 ===
                    this.rows[c].results && a.push(c);
                return a
            };
            return k
        }(),
        ma = function() {
            var k = function(b) {
                    var d = document.createElement(_$_43fc[20]);
                    if (0 < b.options.pagination && b.options.paginationOptions && 0 < b.options.paginationOptions.length) {
                        b.paginationDropdown = document.createElement(_$_43fc[390]);
                        b.paginationDropdown.classList.add(_$_43fc[391]);
                        b.paginationDropdown.onchange = function() {
                            b.options.pagination = parseInt(this.value);
                            b.page(0)
                        };
                        for (var e = 0; e < b.options.paginationOptions.length; e++) {
                            var f = document.createElement(_$_43fc[392]);
                            f.value = b.options.paginationOptions[e];
                            f.innerHTML = b.options.paginationOptions[e];
                            b.paginationDropdown.appendChild(f)
                        }
                        b.paginationDropdown.value = b.options.pagination;
                        d.appendChild(document.createTextNode(z(_$_43fc[393])));
                        d.appendChild(b.paginationDropdown);
                        d.appendChild(document.createTextNode(z(_$_43fc[394])))
                    }
                    return d
                },
                a = function(b, d) {
                    var e = this.merged.rows;
                    if (e[d]) {
                        for (; 0 < d && e[d - 1];) d--;
                        for (; e[d];) b.push(d++)
                    } else b.push(d)
                },
                c = function(b) {
                    b.search = c.update;
                    b.resetSearch = c.reset;
                    b.updateSearch =
                        c.refresh;
                    b.showSearch = c.show;
                    b.hideSearch = c.hide
                };
            c.execute = function(b, d) {
                null === b ? b = this.searchInput.value || _$_43fc[3] : b !== this.searchInput.value && (this.searchInput.value = b);
                this.pageNumber = 0;
                var e = [],
                    f = null;
                d = Y.execute.call(this, d);
                if (b) try {
                    b = new RegExp(b, _$_43fc[377]);
                    for (var g = 0; g < this.rows.length; g++) {
                        f = !1;
                        if (!d || 0 <= d.indexOf(g))
                            for (var h = 0; h < this.options.columns.length; h++) {
                                var l = _$_43fc[3] + K.call(this, h, g),
                                    n = _$_43fc[3] + this.getLabelFromCoords(h, g);
                                if (l.match(b) || n.match(b)) f = !0
                            }
                        1 == f && a.call(this,
                            e, g)
                    }
                } catch (q) {} else e = d;
                if (e.length) {
                    for (g = 0; g < this.rows.length; g++) !0 === this.rows[g].results && e.push(g);
                    e.sort(function(q, p) {
                        return q - p
                    })
                }
                return e
            };
            c.update = function(b) {
                sa.reset.call(this);
                this.resetSelection();
                this.resetBorders();
                var d = c.execute.call(this, b);
                if (typeof this.parent.config.onbeforesearch == _$_43fc[85]) {
                    var e = B.call(this.parent, _$_43fc[396], this, b, d);
                    if (e) d = e;
                    else if (!1 === e) return !1
                }
                this.pageNumber = 0;
                d && d.length < this.rows.length && sa[_$_43fc[11]].call(this, d);
                C.resetY.call(this);
                W.refresh.call(this);
                B.call(this.parent, _$_43fc[397], this, b, d)
            };
            c.refresh = function() {
                C.resetY.call(this);
                W.refresh.call(this)
            };
            c.reset = function() {
                this.searchInput.value = _$_43fc[3];
                sa.reset.call(this);
                C.resetY.call(this);
                W.refresh.call(this)
            };
            c.show = function() {
                this.options.search = !0;
                this.searchContainer.style.display = _$_43fc[3]
            };
            c.hide = function() {
                this.options.search = !1;
                this.searchContainer.style.display = _$_43fc[40]
            };
            c.build = function() {
                this.searchContainer = document.createElement(_$_43fc[20]);
                this.searchContainer.classList.add(_$_43fc[398]);
                this.searchContainer.appendChild(k(this));
                var b = this.searchContainer,
                    d = b.appendChild,
                    e = document.createElement(_$_43fc[20]);
                e.innerHTML = z(_$_43fc[380]) + _$_43fc[114];
                this.searchInput = document.createElement(_$_43fc[220]);
                this.searchInput.classList.add(_$_43fc[395]);
                e.appendChild(this.searchInput);
                d.call(b, e);
                0 == this.options.search && (this.searchContainer.style.display = _$_43fc[40]);
                this.element.insertBefore(this.searchContainer, this.element.firstChild)
            };
            return c
        }(),
        Na = function() {
            var k = function(a) {
                a.contextmenu =
                    document.createElement(_$_43fc[20]);
                a.contextmenu.className = _$_43fc[399];
                jSuites.contextmenu(a.contextmenu, {
                    onclick: function(c, b, d) {
                        c.close()
                    }
                });
                return a.contextmenu
            };
            k.open = function(a, c) {
                var b = a.target.getAttribute(_$_43fc[42]),
                    d = a.target.getAttribute(_$_43fc[43]),
                    e = k[_$_43fc[51]](this, b, d, a, c[0], c[1], c[2]);
                if (typeof this.parent.config.contextMenu == _$_43fc[85]) {
                    var f = this.parent.config.contextMenu(this, b, d, a, e, c[0], c[1], c[2]);
                    if (f) e = f;
                    else if (!1 === f) return !1
                }
                a.preventDefault();
                f = ja.execute.call(this.parent,
                    _$_43fc[86], [this, b, d, a, e, c[0], c[1], c[2]]);
                e = f[4];
                this.parent.contextmenu.contextmenu.open(a, e)
            };
            k[_$_43fc[51]] = function(a, c, b, d, e, f, g) {
                var h = [],
                    l = a.parent.config,
                    n = -1 != navigator.userAgent.indexOf(_$_43fc[400]) ? _$_43fc[401] : _$_43fc[402];
                e == _$_43fc[403] && (1 == l.allowRenameWorksheet && h.push({
                    title: z(_$_43fc[404]),
                    onclick: function() {
                        var p = prompt(z(_$_43fc[404]), d.target.innerText);
                        p && a.parent.renameWorksheet(f, p)
                    }
                }), 1 == l.allowDeleteWorksheet && h.push({
                    title: z(_$_43fc[405]),
                    onclick: function() {
                        confirm(z(_$_43fc[406]),
                            d.target.innerText) && a.parent.deleteWorksheet(f)
                    }
                }), h.push({
                    type: _$_43fc[407]
                }));
                e == _$_43fc[408] && (h.push({
                    title: z(_$_43fc[409]),
                    onclick: function() {
                        var p = prompt(z(_$_43fc[409]), d.target.innerText);
                        a.setNestedCell(f, g, {
                            title: p
                        })
                    }
                }), h.push({
                    type: _$_43fc[407]
                }));
                if (e == _$_43fc[410] || e == _$_43fc[411] || e == _$_43fc[412] || e == _$_43fc[408]) h.push({
                    title: z(_$_43fc[413]),
                    icon: _$_43fc[414],
                    shortcut: n + _$_43fc[415],
                    onclick: function() {
                        a.cut()
                    }
                }), h.push({
                    title: z(_$_43fc[416]),
                    icon: _$_43fc[417],
                    shortcut: n + _$_43fc[418],
                    onclick: function() {
                        a.copy()
                    }
                }), navigator && navigator.clipboard && navigator.clipboard.readText && h.push({
                    title: z(_$_43fc[419]),
                    icon: _$_43fc[420],
                    shortcut: n + _$_43fc[421],
                    onclick: function() {
                        a.selectedCell && navigator.clipboard.readText().then(function(p) {
                            p && a.paste(a.selectedCell[0], a.selectedCell[1], p)
                        })
                    }
                }), h.push({
                    type: _$_43fc[407]
                });
                e == _$_43fc[410] && (1 == a.options.allowInsertColumn && h.push({
                    title: z(_$_43fc[422]),
                    onclick: function() {
                        a.insertColumn(1, parseInt(f), 1)
                    }
                }), 1 == a.options.allowInsertColumn && h.push({
                    title: z(_$_43fc[423]),
                    onclick: function() {
                        a.insertColumn(1, parseInt(f), 0)
                    }
                }), 1 == a.options.allowDeleteColumn && h.push({
                    title: z(_$_43fc[424]),
                    onclick: function() {
                        a.deleteColumn(a.getSelectedColumns().length ? void 0 : parseInt(c))
                    }
                }), 1 == a.options.allowRenameColumn && h.push({
                    title: z(_$_43fc[425]),
                    onclick: function() {
                        a.setHeader(f)
                    }
                }), a.options.data.length || (h.push({
                    type: _$_43fc[407]
                }), h.push({
                    title: z(_$_43fc[426]),
                    onclick: function() {
                        a.insertRow(0)
                    }
                })), 1 == a.options.columnSorting && (h.push({
                    type: _$_43fc[407]
                }), h.push({
                    title: z(_$_43fc[427]),
                    onclick: function() {
                        a.orderBy(f, 0)
                    }
                }), h.push({
                    title: z(_$_43fc[428]),
                    onclick: function() {
                        a.orderBy(f, 1)
                    }
                }), h.push({
                    type: _$_43fc[407]
                })));
                if (e == _$_43fc[412] || e == _$_43fc[411]) 1 == a.options.allowInsertRow && (h.push({
                        title: z(_$_43fc[429]),
                        onclick: function() {
                            a.insertRow(1, parseInt(b), 1)
                        }
                    }), h.push({
                        title: z(_$_43fc[430]),
                        onclick: function() {
                            a.insertRow(1, parseInt(b))
                        }
                    })), 1 == a.options.allowDeleteRow && h.push({
                        title: z(_$_43fc[431]),
                        onclick: function() {
                            a.deleteRow(a.getSelectedRows().length ? void 0 : parseInt(b))
                        }
                    }),
                    h.push({
                        type: _$_43fc[407]
                    });
                if (e == _$_43fc[412] && 1 == a.options.allowComments) {
                    var q = a.records[g][f].element.getAttribute(_$_43fc[264]) || _$_43fc[3];
                    h.push({
                        title: q ? z(_$_43fc[432]) : z(_$_43fc[433]),
                        icon: _$_43fc[434],
                        onclick: function() {
                            var p = A.getColumnNameFromCoords(f, g),
                                r = prompt(z(_$_43fc[435]), q);
                            r && a.setComments(p, r)
                        }
                    });
                    q && h.push({
                        title: z(_$_43fc[436]),
                        onclick: function() {
                            var p = A.getColumnNameFromCoords(f, g);
                            a.setComments(p, _$_43fc[3])
                        }
                    });
                    h.push({
                        type: _$_43fc[407]
                    })
                }
                l.allowExport && h.push({
                    title: z(_$_43fc[437]),
                    icon: _$_43fc[438],
                    shortcut: n + _$_43fc[439],
                    onclick: function() {
                        a.download()
                    }
                });
                l.about && h.push({
                    title: z(_$_43fc[440]),
                    icon: _$_43fc[441],
                    onclick: function() {
                        !0 === l.about ? alert(F().print()) : alert(l.about)
                    }
                });
                return h
            };
            return k
        }(),
        ca = function() {
            var k = function(a) {
                a.showToolbar = function() {
                    k.show.call(a.parent)
                };
                a.hideToolbar = function() {
                    k.hide.call(a.parent)
                };
                a.refreshToolbar = function() {
                    k.update.call(a.parent, a)
                }
            };
            k[_$_43fc[11]] = function(a) {
                this.toolbar.innerHTML = _$_43fc[3];
                this.config.toolbar = k[_$_43fc[51]].call(this,
                    a);
                k.show.call(this)
            };
            k[_$_43fc[51]] = function(a) {
                a || (a = this.config.toolbar);
                a ? Array.isArray(a) ? a = {
                    items: a
                } : typeof a === _$_43fc[85] && (a = a({
                    items: k.getDefault()
                })) : a = {};
                typeof a !== _$_43fc[84] && (a = {});
                a.items || (a.items = k.getDefault());
                typeof a.responsive == _$_43fc[13] && (a.responsive = !0, a.bottom = !1, a.maxWidth = this.element.offsetWidth);
                return a
            };
            k.show = function() {
                this.toolbar.innerHTML || k.build.call(this);
                this.toolbar.style.display = _$_43fc[3];
                this.element.classList.add(_$_43fc[442])
            };
            k.hide = function() {
                this.toolbar.style.display =
                    _$_43fc[40];
                this.element.classList.remove(_$_43fc[442])
            };
            k.update = function(a, c) {
                if (c = this.toolbarInstance)
                    if (c.update(a), c.options.responsive) {
                        var b = parseInt(a.content.style.maxWidth);
                        if (!b || this.config.fullscreen) b = a.element.offsetWidth;
                        c.options.maxWidth = b;
                        c.refresh()
                    }
            };
            k.build = function() {
                for (var a = k[_$_43fc[51]].call(this), c = 0; c < a.items.length; c++)
                    if (a.items[c].type == _$_43fc[390]) {
                        if (!a.items[c].options && a.items[c].v) {
                            a.items[c].options = [];
                            for (var b = 0; b < a.items[c].v.length; b++) a.items[c].options.push(a.items[c].v[b]);
                            a.items[c].k && (a.items[c].onchange = function(d, e, f, g) {
                                t.current.setStyle(t.current.getSelected(), f.k, g)
                            })
                        }
                    } else a.items[c].type == _$_43fc[255] ? (a.items[c].type = _$_43fc[443], a.items[c].onclick = function(d, e, f) {
                        f.color || jSuites.color(f, {
                            onchange: function(g, h) {
                                t.current.setStyle(t.current.getSelected(), f.k, h)
                            }
                        }).open()
                    }) : !a.items[c].onclick && a.items[c].k && (a.items[c].onclick = function() {
                        t.current.setStyle(t.current.getSelected(), this.k, this.v)
                    });
                (c = ja.execute.call(this, _$_43fc[87], [a])) && (a = c[0]);
                this.toolbarInstance =
                    jSuites.toolbar(this.toolbar, a);
                this.toolbarInstance.application = this;
                a = this.element.tabs.getActive();
                this.toolbarInstance.update(this.worksheets[a])
            };
            k.getDefault = function() {
                return [{
                    content: _$_43fc[444],
                    onclick: function() {
                        t.current && t.current.undo()
                    }
                }, {
                    content: _$_43fc[445],
                    onclick: function() {
                        t.current && t.current.redo()
                    }
                }, {
                    content: _$_43fc[438],
                    onclick: function() {
                        t.current && t.current.download()
                    }
                }, {
                    type: _$_43fc[446]
                }, {
                    type: _$_43fc[390],
                    width: _$_43fc[447],
                    options: [_$_43fc[448], _$_43fc[449], _$_43fc[450]],
                    render: function(a) {
                        return _$_43fc[451] + a + _$_43fc[226] + a + _$_43fc[452]
                    },
                    onchange: function(a, c, b, d) {
                        t.current && t.current.setStyle(t.current.getSelected(), _$_43fc[453], d)
                    }
                }, {
                    type: _$_43fc[390],
                    width: _$_43fc[454],
                    content: _$_43fc[455],
                    options: [_$_43fc[456], _$_43fc[457], _$_43fc[458], _$_43fc[459], _$_43fc[460]],
                    render: function(a) {
                        return _$_43fc[461] + a + _$_43fc[226] + a + _$_43fc[452]
                    },
                    onchange: function(a, c, b, d) {
                        t.current && t.current.setStyle(t.current.getSelected(), _$_43fc[462], d)
                    }
                }, {
                    type: _$_43fc[390],
                    columns: 4,
                    options: [_$_43fc[463], _$_43fc[464], _$_43fc[465], _$_43fc[466]],
                    render: function(a) {
                        return _$_43fc[467] + a + _$_43fc[468]
                    },
                    onchange: function(a, c, b, d) {
                        t.current && t.current.setStyle(t.current.getSelected(), _$_43fc[469], d.split(_$_43fc[470])[2])
                    }
                }, {
                    type: _$_43fc[443],
                    content: _$_43fc[471],
                    k: _$_43fc[472],
                    v: _$_43fc[473]
                }, {
                    type: _$_43fc[255],
                    content: _$_43fc[474],
                    k: _$_43fc[255]
                }, {
                    type: _$_43fc[255],
                    content: _$_43fc[475],
                    k: _$_43fc[476]
                }, {
                    type: _$_43fc[390],
                    content: _$_43fc[477],
                    width: _$_43fc[478],
                    options: [_$_43fc[479],
                        _$_43fc[480], _$_43fc[481], _$_43fc[482]
                    ],
                    onchange: function(a, c, b, d, e) {
                        a = t.current.table;
                        a.classList.remove(_$_43fc[483]);
                        a.classList.remove(_$_43fc[484]);
                        a.classList.remove(_$_43fc[485]);
                        1 == e ? a.classList.add(_$_43fc[483]) : 2 == e ? a.classList.add(_$_43fc[484]) : 3 == e && a.classList.add(_$_43fc[485]);
                        t.current.refreshBorders()
                    }
                }, {
                    content: _$_43fc[15],
                    onclick: function(a, c, b) {
                        b.children[0].innerText == _$_43fc[15] ? (t.current.fullscreen(!0), b.children[0].innerText = _$_43fc[486]) : (t.current.fullscreen(!1), b.children[0].innerText =
                            _$_43fc[15])
                    },
                    tooltip: _$_43fc[487],
                    updateState: function(a, c, b, d) {
                        b.children[0].innerText = 1 == d.parent.config.fullscreen ? _$_43fc[486] : _$_43fc[15]
                    }
                }, {
                    type: _$_43fc[390],
                    data: [_$_43fc[488], _$_43fc[489], _$_43fc[490], _$_43fc[491], _$_43fc[492], _$_43fc[493], _$_43fc[494], _$_43fc[495], _$_43fc[496], _$_43fc[497]],
                    columns: 5,
                    render: function(a) {
                        return _$_43fc[467] + a + _$_43fc[468]
                    },
                    right: !0,
                    onchange: function(a, c, b, d, e) {
                        if (a = t.current.getHighlighted()) {
                            b = c.thickness || 1;
                            c = c.color || _$_43fc[498];
                            e = {};
                            for (var f = a[0], g =
                                    a[1], h = a[2], l = a[3], n = a[1]; n <= a[3]; n++)
                                for (var q = a[0]; q <= a[2]; q++) {
                                    var p = A.getColumnNameFromCoords(q, n);
                                    e[p] || (e[p] = _$_43fc[3]);
                                    e[p] = d != _$_43fc[493] && d != _$_43fc[489] || q != f ? (d == _$_43fc[490] || d == _$_43fc[492]) && q > f ? e[p] + (_$_43fc[499] + b + _$_43fc[500] + c + _$_43fc[295]) : d == _$_43fc[488] ? e[p] + (_$_43fc[499] + b + _$_43fc[500] + c + _$_43fc[295]) : e[p] + _$_43fc[501] : e[p] + (_$_43fc[499] + b + _$_43fc[500] + c + _$_43fc[295]);
                                    e[p] = d != _$_43fc[488] && d != _$_43fc[495] && d != _$_43fc[489] || q != h ? e[p] + _$_43fc[503] : e[p] + (_$_43fc[502] + b + _$_43fc[500] +
                                        c + _$_43fc[295]);
                                    e[p] = d != _$_43fc[494] && d != _$_43fc[489] || n != g ? (d == _$_43fc[490] || d == _$_43fc[491]) && n > g ? e[p] + (_$_43fc[504] + b + _$_43fc[500] + c + _$_43fc[295]) : d == _$_43fc[488] ? e[p] + (_$_43fc[504] + b + _$_43fc[500] + c + _$_43fc[295]) : e[p] + _$_43fc[505] : e[p] + (_$_43fc[504] + b + _$_43fc[500] + c + _$_43fc[295]);
                                    e[p] = d != _$_43fc[488] && d != _$_43fc[496] && d != _$_43fc[489] || n != l ? e[p] + _$_43fc[507] : e[p] + (_$_43fc[506] + b + _$_43fc[500] + c + _$_43fc[295])
                                }
                            Object.keys(e) && t.current.setStyle(e, null, null, !0)
                        }
                    },
                    onload: function(a, c) {
                        var b = document.createElement(_$_43fc[20]),
                            d = document.createElement(_$_43fc[20]);
                        b.appendChild(d);
                        var e = jSuites.color(d, {
                            closeOnChange: !1,
                            onchange: function(f, g) {
                                f.parentNode.children[1].style.color = g;
                                c.color = g
                            }
                        });
                        d = document.createElement(_$_43fc[443]);
                        d.classList.add(_$_43fc[508]);
                        d.innerHTML = _$_43fc[509];
                        d.onclick = function() {
                            e.open()
                        };
                        b.appendChild(d);
                        a.children[1].appendChild(b);
                        d = document.createElement(_$_43fc[20]);
                        jSuites.picker(d, {
                            type: _$_43fc[390],
                            data: [1, 2, 3, 4, 5],
                            render: function(f) {
                                return _$_43fc[510] + f + _$_43fc[511]
                            },
                            onchange: function(f,
                                g, h, l) {
                                c.thickness = l
                            },
                            width: _$_43fc[512]
                        });
                        a.children[1].appendChild(d);
                        d = document.createElement(_$_43fc[20]);
                        d.style.flex = _$_43fc[513];
                        a.children[1].appendChild(d)
                    }
                }, {
                    content: _$_43fc[514],
                    onclick: function(a, c, b) {
                        b.children[0].innerText == _$_43fc[514] ? (ma.show.call(t.current), b.children[0].innerText = _$_43fc[515]) : (ma.hide.call(t.current), b.children[0].innerText = _$_43fc[514])
                    },
                    tooltip: _$_43fc[516],
                    updateState: function(a, c, b, d) {
                        b.children[0].innerText = 1 == d.options.search ? _$_43fc[515] : _$_43fc[514]
                    }
                }]
            };
            return k
        }(),
        ea = function() {
            var k = function(a) {
                a.createWorksheet = function(c) {
                    return a.parent.createWorksheet(c)
                };
                a.deleteWorksheet = function(c) {
                    return a.parent.deleteWorksheet(c)
                };
                a.renameWorksheet = function(c, b) {
                    return a.parent.renameWorksheet(c, b)
                };
                a.moveWorksheet = function(c, b) {
                    return a.parent.moveWorksheet(c, b)
                };
                a.openWorksheet = function() {
                    return a.parent.openWorksheet(a.parent.getWorksheet(a))
                };
                a.getWorksheet = function(c) {
                    return a.parent.getWorksheet(c)
                };
                a.getWorksheetActive = function() {
                    return a.parent.getWorksheetActive()
                };
                a.getWorksheetName = function() {
                    return a.options.worksheetName.toUpperCase()
                }
            };
            k.nextName = function() {
                var a = 1,
                    c, b = t.spreadsheet;
                if (b.length)
                    for (var d = 0; d < b.length; d++) {
                        var e = b[d].worksheets;
                        if (e.length)
                            for (var f = 0; f < e.length; f++) e[f].options.worksheetName && (c = e[f].options.worksheetName.match(/(\d+)/)) && (c = parseInt(c[0]) + 1, c > a && (a = c))
                    }
                return a
            };
            k.createWorksheet = function(a) {
                if (!N.call(this)) return !1;
                a || (a = {});
                var c = this.worksheets.length,
                    b = B.call(this, _$_43fc[517], a, c);
                if (typeof b === _$_43fc[84]) a = b;
                else if (!1 ===
                    b) return !1;
                a.data || a.minDimensions || (a.minDimensions = [8, 8]);
                a.worksheetId || (a.worksheetId = jSuites.guid().substring(0, 8));
                a.worksheetName || (a.worksheetName = _$_43fc[359] + k.nextName());
                D(a.worksheetName) ? jSuites.notification({
                    error: _$_43fc[513],
                    message: _$_43fc[518]
                }) : (b = ka.worksheet(this, a), b.onload(), G.call(b, _$_43fc[519], [a]), B.call(this, _$_43fc[520], b, a, c))
            };
            k.deleteWorksheet = function(a) {
                var c = this.worksheets[a];
                if (!N.call(this, c)) return !1;
                D(c.options.worksheetName, null);
                this.element.tabs.deleteElement(a);
                this.worksheets.splice(a, 1);
                G.call(c, _$_43fc[521], [a]);
                B.call(this, _$_43fc[522], c, a)
            };
            k.renameWorksheet = function(a, c) {
                var b = this.worksheets[a];
                if (!N.call(this, b)) return !1;
                var d = b.options.worksheetName;
                if (d.toLowerCase() == c.toLowerCase()) return !1;
                c = c.replace(/[^0-9A-Za-z_\s^]+/gi, _$_43fc[3]);
                Number(c) == c && (c = _$_43fc[359] + Number(c));
                D(c) ? alert(_$_43fc[523]) : (D(d, null), this.element.tabs.rename(a, c), b.options.worksheetName = c, D(c, b), Q.call(this, {
                        worksheet: b,
                        action: _$_43fc[80],
                        index: a,
                        oldValue: d,
                        newValue: c
                    }),
                    G.call(b, _$_43fc[80], {
                        worksheet: a,
                        newValue: c
                    }), Ea.updateWorksheetName.call(b, d, c), S.updateWorksheetNames.call(b, d, c), B.call(this, _$_43fc[524], b, a, c, d))
            };
            k.updateWorksheet = function(a, c) {
                var b = this.worksheets;
                if (!N.call(this)) return !1;
                this.worksheets.splice(c, 0, b.splice(a, 1)[0]);
                Q.call(this, {
                    worksheet: b[c],
                    action: _$_43fc[81],
                    f: a,
                    t: c
                });
                G.call(b[c], _$_43fc[81], {
                    f: a,
                    t: c
                });
                B.call(this, _$_43fc[525], b[c], a, c)
            };
            k.moveWorksheet = function(a, c) {
                this.element.tabs.move(a, c)
            };
            k.openWorksheet = function(a) {
                var c = this.worksheets[a];
                !c || t.current && t.current == c || (this.element.tabs.getActive() != a ? this.element.tabs.open(a) : this.element.tabs.setBorder(a), t.current && t.current.resetSelection(), t.current = c, t.current.refreshBorders(), P.call(t.current), k.state.call(this, a), ca.update.call(this, t.current), B.call(this, _$_43fc[526], c, a))
            };
            k.getWorksheet = function(a) {
                if (a && typeof a == _$_43fc[84]) {
                    if (a = this.worksheets.indexOf(a), 0 <= a) return a
                } else
                    for (var c = 0; c < this.worksheets.length; c++)
                        if (a === this.worksheets[c].options.worksheetId) return c;
                return !1
            };
            k.getWorksheetActive = function() {
                return this.element.tabs.getActive()
            };
            k.getWorksheetInstance = function(a) {
                return a === _$_43fc[3] || typeof a == _$_43fc[17] ? (a = k.getWorksheet.call(this, a), !1 === a ? !1 : this.worksheets[a]) : this.worksheets[a]
            };
            k.state = function(a) {
                try {
                    if (typeof localStorage !== _$_43fc[13]) {
                        var c = this.config.cloud ? this.config.cloud : this.element.id;
                        if (c)
                            if (typeof a !== _$_43fc[13]) localStorage.setItem(_$_43fc[527] + c, a);
                            else {
                                var b = parseInt(localStorage.getItem(_$_43fc[527] + c));
                                k.openWorksheet.call(this,
                                    b)
                            }
                    }
                } catch (d) {}
            };
            return k
        }(),
        A = function() {
            var k = {
                getCaretIndex: function(a) {
                    var c = 0,
                        b = (this.config.root ? this.config.root : window).getSelection();
                    b && 0 !== b.rangeCount && (c = b.getRangeAt(0), b = c.cloneRange(), b.selectNodeContents(a), b.setEnd(c.endContainer, c.endOffset), c = b.toString().length);
                    return c
                },
                invert: function(a) {
                    for (var c = [], b = Object.keys(a), d = 0; d < b.length; d++) c[a[b[d]]] = b[d];
                    return c
                },
                getColumnName: function(a) {
                    var c = _$_43fc[3];
                    701 < a ? (c += String.fromCharCode(64 + parseInt(a / 676)), c += String.fromCharCode(64 +
                        parseInt(a % 676 / 26))) : 25 < a && (c += String.fromCharCode(64 + parseInt(a / 26)));
                    return c += String.fromCharCode(65 + a % 26)
                },
                getColumnNameFromCoords: function(a, c) {
                    return k.getColumnName(parseInt(a)) + (parseInt(c) + 1)
                },
                getCoordsFromColumnName: function(a) {
                    var c = /^[a-zA-Z]+/.exec(a);
                    if (c) {
                        for (var b = 0, d = 0; d < c[0].length; d++) b += parseInt(c[0].charCodeAt(d) - 64) * Math.pow(26, c[0].length - 1 - d);
                        b--;
                        0 > b && (b = 0);
                        a = parseInt(/[0-9]+$/.exec(a)) || null;
                        0 < a && a--;
                        return [b, a]
                    }
                }
            };
            k.shiftFormula = S.shiftFormula;
            k.createFromTable = function(a,
                c) {
                if (a.tagName != _$_43fc[528]) console.log(_$_43fc[529]);
                else {
                    c || (c = {});
                    c.columns = [];
                    c.data = [];
                    var b = a.querySelectorAll(_$_43fc[530]);
                    if (b.length)
                        for (var d = 0; d < b.length; d++) {
                            var e = b[d].style.width;
                            e || (e = b[d].getAttribute(_$_43fc[152]));
                            e && (c.columns[d] || (c.columns[d] = {}), c.columns[d].width = e)
                        }
                    e = function(u) {
                        var x = u.getBoundingClientRect();
                        x = 50 < x.width ? x.width : 50;
                        c.columns[d] || (c.columns[d] = {});
                        u.getAttribute(_$_43fc[531]) ? c.columns[d].type = u.getAttribute(_$_43fc[531]) : c.columns[d].type = _$_43fc[56];
                        c.columns[d].width = x + _$_43fc[44];
                        c.columns[d].title = u.innerHTML;
                        c.columns[d].align = u.style.textAlign || (c.defaultColAlign ? c.defaultColAlign : _$_43fc[53]);
                        u.classList.contains(_$_43fc[532]) && (c.columns[d].readOnly = !0);
                        if (x = u.getAttribute(_$_43fc[533])) c.columns[d].name = x;
                        if (x = u.getAttribute(_$_43fc[534])) c.columns[d].id = x
                    };
                    var f = [],
                        g = a.querySelectorAll(_$_43fc[535]);
                    if (g.length) {
                        for (b = 0; b < g.length - 1; b++) {
                            var h = [];
                            for (d = 0; d < g[b].children.length; d++) {
                                var l = {
                                    title: g[b].children[d].innerText,
                                    colspan: g[b].children[d].getAttribute(_$_43fc[52]) ||
                                        1
                                };
                                h.push(l)
                            }
                            f.push(h)
                        }
                        g = g[g.length - 1].children;
                        for (d = 0; d < g.length; d++) e(g[d])
                    }
                    l = 0;
                    var n = {},
                        q = {},
                        p = {},
                        r = {},
                        w = null;
                    h = a.querySelectorAll(_$_43fc[536]);
                    for (b = 0; b < h.length; b++)
                        if (c.data[l] = [], 1 != c.parseTableFirstRowAsHeader || g.length || 0 != b) {
                            for (d = 0; d < h[b].children.length; d++) {
                                var m = h[b].children[d].getAttribute(_$_43fc[537]);
                                m ? m.substr(0, 1) != _$_43fc[12] && (m = _$_43fc[12] + m) : m = h[b].children[d].innerHTML;
                                c.data[l].push(m);
                                m = A.getColumnNameFromCoords(d, b);
                                (w = h[b].children[d].getAttribute(_$_43fc[538])) && (r[m] =
                                    w);
                                w = parseInt(h[b].children[d].getAttribute(_$_43fc[52])) || 0;
                                var v = parseInt(h[b].children[d].getAttribute(_$_43fc[164])) || 0;
                                if (w || v) n[m] = [w || 1, v || 1];
                                if (w = h[b].children[d].style && h[b].children[d].style.display == _$_43fc[40]) h[b].children[d].style.display = _$_43fc[3];
                                (w = h[b].children[d].getAttribute(_$_43fc[98])) && (p[m] = w);
                                h[b].children[d].classList.contains(_$_43fc[539]) && (p[m] = p[m] ? p[m] + _$_43fc[540] : _$_43fc[541])
                            }
                            h[b].style && h[b].style.height && (q[b] = {
                                height: h[b].style.height
                            });
                            l++
                        } else
                            for (d = 0; d < h[b].children.length; d++) e(h[b].children[d]);
                    0 < Object.keys(f).length && (c.nestedHeaders = f);
                    0 < Object.keys(p).length && (c.style = p);
                    0 < Object.keys(n).length && (c.mergeCells = n);
                    0 < Object.keys(q).length && (c.rows = q);
                    0 < Object.keys(r).length && (c.classes = r);
                    h = a.querySelectorAll(_$_43fc[542]);
                    if (h.length) {
                        a = [];
                        for (b = 0; b < h.length; b++) {
                            e = [];
                            for (d = 0; d < h[b].children.length; d++) e.push(h[b].children[d].innerText);
                            a.push(e)
                        }
                        0 < Object.keys(a).length && (c.footers = a)
                    }
                    if (1 == c.parseTableAutoCellType)
                        for (a = [], d = 0; d < c.columns.length; d++) {
                            f = e = !0;
                            a[d] = [];
                            for (b = 0; b < c.data.length; b++)
                                if (m =
                                    c.data[b][d], a[d][m] || (a[d][m] = 0), a[d][m]++, 25 < m.length && (e = !1), 10 == m.length) {
                                    if (m.substr(4, 1) != _$_43fc[185] || m.substr(7, 1) != _$_43fc[185]) f = !1
                                } else f = !1;
                            b = Object.keys(a[d]).length;
                            f ? c.columns[d].type = _$_43fc[184] : 1 == e && 1 < b && b <= parseInt(.1 * c.data.length) && (c.columns[d].type = _$_43fc[58], c.columns[d].source = Object.keys(a[d]))
                        }
                    return c
                }
            };
            k.injectArray = function(a, c, b) {
                return a.slice(0, c).concat(b).concat(a.slice(c))
            };
            k.parseCSV = function(a, c) {
                c = c || _$_43fc[25];
                for (var b = 0, d = 0, e = [
                            []
                        ], f = 0, g = null, h = !1, l = !1,
                        n = 0; n < a.length; n++)
                    if (e[d] || (e[d] = []), e[d][b] || (e[d][b] = _$_43fc[3]), a[n] != _$_43fc[543])
                        if (a[n] != _$_43fc[544] && a[n] != c || 0 != h && 1 != l && g) {
                            a[n] == _$_43fc[187] && (h = !h);
                            if (null === g) {
                                if (g = h, 1 == g) continue
                            } else if (!0 === g && !l && a[n] == _$_43fc[187]) {
                                a[n + 1] == _$_43fc[187] ? (h = !0, e[d][b] += a[n], n++) : l = !0;
                                continue
                            }
                            e[d][b] += a[n]
                        } else {
                            g = null;
                            l = h = !1;
                            if (e[d][b][0] == _$_43fc[187]) {
                                var q = e[d][b].trim();
                                q[q.length - 1] == _$_43fc[187] && (e[d][b] = q.substr(1, q.length - 2))
                            }
                            a[n] == _$_43fc[544] ? (b = 0, d++) : (b++, b > f && (f = b))
                        } for (a = 0; a < e.length; a++)
                    for (n =
                        0; n <= f; n++) void 0 === e[a][n] && (e[a][n] = _$_43fc[3]);
                return e
            };
            k.getTokensFromRange = S.getTokensFromRange;
            k.getRangeFromTokens = S.getRangeFromTokens;
            return k
        }(),
        xa = function() {
            var k = {},
                a = null,
                c = null,
                b = null,
                d = !1,
                e = null,
                f = null,
                g = null,
                h = function(m) {
                    function v(J) {
                        J.className && (J.classList.contains(_$_43fc[349]) && (y = J), J.classList.contains(_$_43fc[362]) && (H = J), J.classList.contains(_$_43fc[545]) && (L = J));
                        J.tagName == _$_43fc[546] ? u = 1 : J.tagName == _$_43fc[547] ? u = 2 : J.tagName == _$_43fc[548] && (u = 3);
                        J.parentNode && !y && v(J.parentNode)
                    }
                    var u = null,
                        x = null,
                        y = null,
                        H = null,
                        L = null,
                        E = null,
                        ba = null,
                        ra = null;
                    v(m.target);
                    if (null !== L) return !1;
                    if (y)
                        if (m.target.classList.contains(_$_43fc[330])) x = _$_43fc[549];
                        else if (m.target.classList.contains(_$_43fc[339])) x = _$_43fc[138];
                    else if (m.target.classList.contains(_$_43fc[267])) x = _$_43fc[411], E = parseInt(m.target.getAttribute(_$_43fc[43]));
                    else if (m.target.parentNode && m.target.parentNode.classList.contains(_$_43fc[177])) E = m.target.getAttribute(_$_43fc[181]), ba = m.target.getAttribute(_$_43fc[182]), null ===
                        E ? x = _$_43fc[549] : (x = _$_43fc[408], E = parseInt(E), ba = parseInt(ba));
                    else if (m.target.parentNode && m.target.parentNode.classList.contains(_$_43fc[146])) x = _$_43fc[550];
                    else if (m.target.classList.contains(_$_43fc[150])) x = _$_43fc[550];
                    else if (m.target.parentNode && m.target.parentNode.classList.contains(_$_43fc[551])) x = _$_43fc[403], E = Array.prototype.indexOf.call(m.target.parentNode.children, m.target);
                    else if (1 == u) x = _$_43fc[410], E = m.target.clientWidth - m.offsetX, m.target.classList.contains(_$_43fc[168]) && 3 < E &&
                        20 > E && (x = _$_43fc[552]), E = m.target.getAttribute(_$_43fc[42]);
                    else if (2 == u) {
                        if (m = l(m, y)) x = _$_43fc[412], E = m[0], ba = m[1], ra = m[2]
                    } else if (3 == u) {
                        if (E = m.target.getAttribute(_$_43fc[42]), ba = m.target.getAttribute(_$_43fc[43]), E || ba) x = _$_43fc[553]
                    } else u || jSuites.findElement(m.target, _$_43fc[554]) && (x = _$_43fc[87]);
                    return [y, H, u, x, E, ba, ra]
                },
                l = function(m, v) {
                    if (m.changedTouches && m.changedTouches[0]) {
                        var u = m.changedTouches[0].clientX;
                        m = m.changedTouches[0].clientY
                    } else u = m.clientX, m = m.clientY;
                    v = (v.spreadsheet.config.root ?
                        v.spreadsheet.config.root : document).elementsFromPoint(u, m);
                    for (var x = 0; x < v.length; x++)
                        if (u = v[x].getAttribute(_$_43fc[42]), m = v[x].getAttribute(_$_43fc[43]), null != u && null != m) return u = parseInt(u), m = parseInt(m), [u, m, v[x]];
                    return !1
                },
                n = function(m) {
                    var v = h(m);
                    if (!1 === v) return !1;
                    v[0] ? v[1] ? t.current != v[1].jexcel && (t.current && t.current.resetSelection(), t.current = v[1].jexcel) : (m = v[0].tabs.getActive(), 0 <= m && (t.current = v[0].spreadsheet.worksheets[m])) : t.current && !jSuites.findElement(m.target, _$_43fc[219]) && (t.current.resetSelection(!0),
                        t.current = null);
                    return v
                };
            k.input = function(m) {
                if (m.target.tagName == _$_43fc[555] && m.target.classList.contains(_$_43fc[92])) {
                    var v = null;
                    t.current ? v = t.current : m.target.getAttribute(_$_43fc[556]) && (v = D(m.target.getAttribute(_$_43fc[556])));
                    v && fa.parse.call(v, m.target)
                }
            };
            k.keydown = function(m) {
                if (t.current) {
                    m.target.tagName == _$_43fc[555] && m.target.classList.contains(_$_43fc[92]) && fa.onkeydown.call(t.current, m);
                    if (27 == m.which) {
                        if (t.current.edition) return t.current.closeEditor(t.current.edition, !1), m.preventDefault(),
                            !1;
                        if (na.event) return na.cancel.call(t.current, m), m.preventDefault(), !1;
                        if (ha.event) return ha.cancel.call(t.current, m), !1;
                        if (1 == Y.isVisible()) return Y.close.call(t.current, !1), m.preventDefault(), !1
                    }
                    if (!m.target.classList.contains(_$_43fc[219]))
                        if (m.target.classList.contains(_$_43fc[381])) {
                            var v = m.target;
                            e && clearTimeout(e);
                            e = setTimeout(function() {
                                Y.search.call(t.current, v.value);
                                e = null
                            }, 500)
                        } else if (m.target.classList.contains(_$_43fc[395])) v = m.target, e && clearTimeout(e), e = setTimeout(function() {
                        t.current.search(v.value);
                        e = null
                    }, 400);
                    else if (t.current.edition) {
                        var u = t.current.edition.getAttribute(_$_43fc[42]);
                        13 == m.which ? t.current.options.columns[u].type == _$_43fc[184] ? t.current.closeEditor(t.current.edition, !0) : t.current.options.columns[u].type != _$_43fc[58] && t.current.options.columns[u].type != _$_43fc[557] && t.current.options.columns[u].type != _$_43fc[434] && (m.altKey ? t.current.parent.input.classList.contains(_$_43fc[558]) || document.execCommand(_$_43fc[559], !1, _$_43fc[560]) : (t.current.closeEditor(t.current.edition, !0),
                            t.current.down(), m.preventDefault())) : 9 == m.which && (t.current.closeEditor(t.current.edition, !0), t.current.right(), m.preventDefault())
                    } else if (t.current.selectedCell)
                        if (33 == m.which) 0 < t.current.options.pagination ? qa.pageUp.call(t.current) : C.pageUp.call(t.current);
                        else if (34 == m.which) 0 < t.current.options.pagination ? qa.pageDown.call(t.current) : C.pageDown.call(t.current);
                    else if (37 == m.which) t.current.left(m.shiftKey, m.ctrlKey), m.preventDefault();
                    else if (39 == m.which) t.current.right(m.shiftKey, m.ctrlKey),
                        m.preventDefault();
                    else if (38 == m.which) t.current.up(m.shiftKey, m.ctrlKey), m.preventDefault();
                    else if (40 == m.which) t.current.down(m.shiftKey, m.ctrlKey), m.preventDefault();
                    else if (36 == m.which) t.current.first(m.shiftKey, m.ctrlKey), m.preventDefault();
                    else if (35 == m.which) t.current.last(m.shiftKey, m.ctrlKey), m.preventDefault();
                    else if (32 == m.which) t.current.setCheckRadioValue(), m.preventDefault();
                    else if (46 == m.which || 8 == m.which) t.current.isEditable() && t.current.setValue(t.current.getSelected(!1, !0), _$_43fc[3]);
                    else if (13 == m.which) m.shiftKey ? t.current.up() : (1 == t.current.options.allowManualInsertRow && t.current.selectedCell[1] == (t.current.results ? t.current.results[t.current.results.length - 1] : t.current.rows.length - 1) && t.current.insertRow(), t.current.selectedCell && t.current.down()), m.preventDefault();
                    else if (9 == m.which) m.shiftKey ? t.current.left() : (u = t.current.selectedCell[0], t.current.right(), 1 == t.current.options.allowInsertColumn && 1 == t.current.options.allowManualInsertColumn && t.current.selectedCell[0] == u &&
                        (t.current.insertColumn(), t.current.right())), m.preventDefault();
                    else if ((m.ctrlKey || m.metaKey) && !m.shiftKey) 65 == m.which ? (t.current.selectAll(), m.preventDefault()) : 83 == m.which ? (t.current.download(), m.preventDefault()) : 89 == m.which ? (t.current.parent.redo(), m.preventDefault()) : 90 == m.which ? (t.current.parent.undo(), m.preventDefault()) : 67 == m.which ? (t.current.copy(), m.preventDefault()) : 88 == m.which ? (t.current.cut(), m.preventDefault()) : 86 == m.which ? k.paste(m) : 189 == m.which && (1 == t.current.options.allowDeleteRow &&
                        b == _$_43fc[411] ? (t.current.deleteRow(), m.preventDefault()) : 1 == t.current.options.allowDeleteColumn && b == _$_43fc[410] && (t.current.deleteColumn(), m.preventDefault()));
                    else if (t.current.selectedCell && t.current.isEditable()) {
                        u = t.current.selectedCell[1];
                        var x = t.current.selectedCell[0];
                        t.current.options.columns[x].type != _$_43fc[18] && (32 == m.keyCode ? t.current.options.columns[x].type == _$_43fc[250] || t.current.options.columns[x].type == _$_43fc[252] ? m.preventDefault() : t.current.openEditor(t.current.records[u][x].element,
                            !0, m) : 113 == m.keyCode ? t.current.openEditor(t.current.records[u][x].element, !1, m) : 1 != m.key.length && m.key != _$_43fc[561] || m.altKey || m.ctrlKey || t.current.records[u] && t.current.openEditor(t.current.records[u][x].element, !0, m))
                    }
                }
            };
            var q = function(m, v, u, x, y, H) {
                if (1 == t.current.parent.input.classList.contains(_$_43fc[92]) && fa.update.call(t.current, m, v, u, x, y)) return !1;
                if (t.current.edition) t.current.closeEditor(t.current.edition, !0);
                else if (b && b !== H) return !1;
                fa.current ? fa.range.call(t.current, m, v, u, x, y) : ua[_$_43fc[11]].call(t.current,
                    m, v, u, x, y)
            };
            k.mousedown = function(m) {
                m = m || window.event;
                var v = m.buttons ? m.buttons : m.button ? m.button : m.which;
                if (t.current)
                    if (1 == Y.isVisible() && (jSuites.findElement(m.target, _$_43fc[385]) || Y.close.call(t.current, !1)), t.current.edition) {
                        if (jSuites.findElement(m.target, _$_43fc[99])) return !1
                    } else if (jSuites.findElement(m.target, _$_43fc[219])) return !1;
                var u = n(m);
                if (!1 === u) return !1;
                g = u;
                if (1 < v)
                    if (u[3] == _$_43fc[410]) {
                        if (t.current.isSelected(u[4], null)) return !1
                    } else if (u[3] == _$_43fc[411]) {
                    if (t.current.isSelected(null,
                            u[4])) return !1
                } else if (u[3] == _$_43fc[412] && t.current.isSelected(u[4], u[5])) return !1;
                if (t.current) {
                    if (u[3] == _$_43fc[138]) t.current.isEditable() && (ha.event = !0);
                    else if (u[3] == _$_43fc[552]) Y.open.call(t.current, u[4]);
                    else {
                        if (1 == u[2])
                            if (1 == t.current.options.columnDrag && 6 > m.target.offsetHeight - m.offsetY) na.start.call(t.current, m);
                            else if (1 == t.current.options.columnResize && 6 > m.target.offsetWidth - m.offsetX) ta.start.call(t.current, m);
                        else {
                            v = t.current.options.columns.length - 1;
                            var x = t.current.options.data.length -
                                1;
                            if (u[3] == _$_43fc[549]) q(0, 0, v, x, m, u[3]), c = a = 0;
                            else if (u[3] == _$_43fc[408]) {
                                var y = la.range.call(t.current, u[5])[u[4]];
                                y[1] > v && (y[1] = v);
                                q(y[0], 0, y[1], x, m, u[3]);
                                a = y[0];
                                c = 0
                            } else(m.shiftKey || m.ctrlKey) && null != a && null != c ? q(a, 0, u[4], x, m, u[3]) : (u[3] == _$_43fc[410] && a == u[4] && 1 == t.current.options.allowRenameColumn && (f = setTimeout(function() {
                                t.current.setHeader(u[4])
                            }, 800)), q(u[4], 0, u[4], x, m, u[3]), a = u[4], c = 0)
                        }
                        if (2 == u[2])
                            if (u[3] == _$_43fc[411]) 1 == t.current.options.rowDrag && 6 > m.target.offsetWidth - m.offsetX ? na.start.call(t.current,
                                m) : 1 == t.current.options.rowResize && 6 > m.target.offsetHeight - m.offsetY ? ta.start.call(t.current, m) : (v = t.current.options.columns.length - 1, x = parseInt(u[4]), (m.shiftKey || m.ctrlKey) && null != a && null != c ? q(0, c, v, x, m, u[3]) : (q(0, x, v, x, m, u[3]), a = 0, c = x));
                            else if (u[3] == _$_43fc[412]) {
                            v = parseInt(u[4]);
                            x = parseInt(u[5]);
                            if (!t.current.edition && (y = t.current.options.columns[u[4]].type, m.target.tagName == _$_43fc[562] && (y == _$_43fc[253] || y == _$_43fc[254]) && m.target.parentNode.classList.contains(_$_43fc[124]))) {
                                var H = m.target.innerText;
                                y == _$_43fc[253] && (H = _$_43fc[563] + H);
                                window.open(H, _$_43fc[564])
                            }(m.shiftKey || m.ctrlKey) && null != a && null != c ? q(a, c, v, x, m, u[3]) : (q(v, x, v, x, m, u[3]), a = v, c = x)
                        }
                    }
                    b = u[3];
                    typeof u[3] !== _$_43fc[13] && B.call(t.current.parent, _$_43fc[565], t.current, u[3], u[4], u[5]);
                    d = !0
                } else d = !1
            };
            k.mouseup = function(m) {
                t.current && (na.event ? na.end.call(t.current, m) : ta.event ? ta.end.call(t.current, m) : ha.event ? ha.end.call(t.current, m) : m.target.classList.contains(_$_43fc[384]) ? Y.close.call(t.current, !0) : t.current.edition && m.target.classList.contains(_$_43fc[566]) &&
                    50 > m.target.clientWidth - m.offsetX && 50 > m.offsetY ? Z.close.call(t.current, null, !0) : m.target.classList.contains(_$_43fc[318]) && (m = m.target.innerText == _$_43fc[215] ? 0 : m.target.innerText == _$_43fc[216] ? m.target.getAttribute(_$_43fc[264]) - 1 : m.target.innerText - 1, qa[_$_43fc[11]].call(t.current, parseInt(m))));
                f && (clearTimeout(f), f = null);
                d = b = !1;
                fa.current && fa.close.call(t.current)
            };
            k.mousemove = function(m) {
                m = m || window.event;
                if (t.current) {
                    var v = m.target.getAttribute(_$_43fc[42]),
                        u = m.target.getAttribute(_$_43fc[43]);
                    if (1 == d) na.event ? na.update.call(t.current, m) : ta.event ? ta.update.call(t.current, m) : La.event && La.update.call(t.current, m);
                    else {
                        var x = m.target.getBoundingClientRect();
                        t.current.cursor && (t.current.cursor.style.cursor = _$_43fc[3], t.current.cursor = null);
                        1 == t.current.options.editable && (v || u) && (1 == t.current.options.rowResize && m.target && v && !u && 5 > x.width - (m.clientX - x.left) ? (t.current.cursor = m.target, t.current.cursor.style.cursor = _$_43fc[567]) : 1 == t.current.options.columnResize && m.target && !v && u && 5 > x.height - (m.clientY -
                            x.top) ? (t.current.cursor = m.target, t.current.cursor.style.cursor = _$_43fc[568]) : 1 == t.current.options.rowDrag && m.target && !v && u && 5 > x.width - (m.clientX - x.left) ? (t.current.cursor = m.target, t.current.cursor.style.cursor = _$_43fc[569]) : 1 == t.current.options.columnDrag && m.target && v && !u && 5 > x.height - (m.clientY - x.top) && (t.current.cursor = m.target, t.current.cursor.style.cursor = _$_43fc[569]))
                    }
                }
            };
            k.mouseover = function(m) {
                m = m || window.event;
                var v = !1;
                if (t.current && 1 == d) {
                    var u = h(m);
                    if (u[0]) {
                        if (u[1] && t.current != u[1].jexcel &&
                            t.current) return !1;
                        if (!na.event && !ta.event)
                            if (ha.event) {
                                var x = m.target.getAttribute(_$_43fc[42]),
                                    y = m.target.getAttribute(_$_43fc[43]);
                                t.current.isSelected(x, y) || null === x || null === y || (ha.call(t.current, x, y), v = !0)
                            } else null !== a && null !== c && (1 == u[2] && (x = a, u[3] == _$_43fc[410] && (x = m.target.getAttribute(_$_43fc[42])), y = t.current.options.data.length - 1, q(a, 0, x, y, m, u[3])), 2 == u[2] && (u[3] == _$_43fc[411] ? (x = t.current.options.columns.length - 1, y = parseInt(m.target.getAttribute(_$_43fc[43])), q(0, c, x, y, m, u[3])) : u[3] ==
                                _$_43fc[412] && (x = m.target.getAttribute(_$_43fc[42]), y = m.target.getAttribute(_$_43fc[43]), x && y && (q(a, c, x, y, m, u[3]), v = !0))))
                    }
                }
                1 == v && !f && C.limited.call(t.current) ? f = setTimeout(function() {
                    var H = t.current.content.getBoundingClientRect();
                    if (m.changedTouches && m.changedTouches[0]) var L = m.changedTouches[0].clientX,
                        E = m.changedTouches[0].clientY;
                    else L = m.clientX, E = m.clientY;
                    50 > H.height - (E - H.top) ? C.pageDown.call(t.current, 1, null, m) : 80 > E - H.top && C.pageUp.call(t.current, 1, null, m);
                    50 > H.width - (L - H.left) ? C.pageRight.call(t.current,
                        1, null, m) : 80 > L - H.left && C.pageLeft.call(t.current, 1, null, m);
                    f = null
                }, 100) : f && (clearTimeout(f), f = null)
            };
            k.dblclick = function(m) {
                if (t.current)
                    if (m.target.classList.contains(_$_43fc[339])) ha.execute.call(t.current, !1);
                    else {
                        var v = h(m);
                        1 == v[2] ? 1 == t.current.options.columnSorting && (m = m.target.getAttribute(_$_43fc[42])) && t.current.orderBy(m) : 2 == v[2] && t.current.isEditable() && !t.current.edition && v[6] && t.current.openEditor(v[6])
                    }
            };
            k.paste = function(m) {
                if (t.current && t.current.selectedCell && !t.current.edition) {
                    var v =
                        m.clipboardData || window.clipboardData;
                    v && (t.current.paste(t.current.selectedCell[0], t.current.selectedCell[1], v.getData(_$_43fc[56])), m && m.preventDefault())
                }
            };
            k.contextmenu = function(m) {
                var v = n(m);
                !1 !== v && (v[1] || (v = g), t.current && !t.current.edition && (v = Array.prototype.slice.call(v, 3), Na.open.call(t.current, m, v)))
            };
            var p = null,
                r = null,
                w = null;
            k.touchstart = function(m) {
                var v = h(m);
                v[0] ? v[1] && t.current != v[1].jexcel && (t.current && t.current.resetSelection(), t.current = v[1].jexcel) : t.current && (t.current.resetSelection(),
                    t.current = null);
                if (t.current && !t.current.edition) {
                    var u = v[4];
                    v = v[5];
                    r = m.touches[0];
                    p = m.touches.length;
                    w = [u, v];
                    if (null !== u && null !== v) {
                        t.current.updateSelectionFromCoords(u, v, u, v);
                        var x = m.target;
                        f = setTimeout(function() {
                            t.current.openEditor(x, !1, m);
                            f = null
                        }, 250)
                    }
                }
            };
            k.touchmove = function(m) {
                f && (clearTimeout(f), f = null);
                if (t.current) {
                    m = m.changedTouches[0];
                    if (r)
                        if (1 < p) {
                            var v = document.elementFromPoint(m.pageX, m.pageY),
                                u = v.getAttribute(_$_43fc[42]);
                            v = v.getAttribute(_$_43fc[43]);
                            null !== u && null !== v && t.current.updateSelectionFromCoords(w[0],
                                w[1], u, v)
                        } else t.current.scrollY.scrollTop += -1 * (m.clientY - r.clientY), t.current.scrollX.scrollLeft += -1 * (m.clientX - r.clientX);
                    r = m
                }
            };
            k.touchend = function(m) {
                f && (clearTimeout(f), f = null);
                w = r = p = null
            };
            k.resize = function() {
                t.current && 1 == t.current.parent.config.fullscreen && C.refresh.call(t.current)
            };
            return k
        }(),
        t = function(k, a, c) {
            a || (a = {});
            if (a.cloud && !c) typeof a.plugins.cloud == _$_43fc[85] ? a.plugins.cloud.load(a, function(f) {
                t(k, f, !0)
            }) : console.error(_$_43fc[570]);
            else {
                if (k.spreadsheet) b = k.spreadsheet, za[_$_43fc[11]].call(b,
                    a);
                else var b = ka.spreadsheet(k, a);
                b.processing = !0;
                1 == b.config.loadingSpin && jSuites.loading.show();
                jSuites.ajax.oncomplete[b.name] = function() {
                    for (var f = 0; f < b.worksheets.length; f++) b.worksheets[f].onload();
                    b.processing = !1;
                    if (Array.isArray(b.queue) && 0 < b.queue.length)
                        for (; f = b.queue.shift();) f[0].updateCell(f[1], f[2], K.call(f[0], f[1], f[2]), !0);
                    b.config.toolbar && ca.show.call(b);
                    for (f = 0; f < b.worksheets.length; f++) W.refresh.call(b.worksheets[f]);
                    if (typeof b.config.onload == _$_43fc[85]) b.config.onload(b);
                    ea.state.call(b);
                    jSuites.loading.hide()
                };
                Array.isArray(a.worksheets) && (a = a.worksheets);
                if (Array.isArray(a)) {
                    c = null;
                    for (var d = [], e = 0; e < a.length; e++) c = ka.worksheet(b, a[e]), d.push(c)
                } else d = ka.worksheet(b, a);
                jSuites.ajax.pending(b.name) || (jSuites.ajax.oncomplete[b.name](), jSuites.ajax.oncomplete[b.name] = null);
                return d
            }
        };
    t.license = null;
    t.setLicense = function(k) {
        t.license = k
    };
    t.spreadsheet = [];
    t.picker = fa;
    t.setDictionary = function(k) {
        jSuites.setDictionary(k)
    };
    t.extensions = {};
    t.setExtensions = function(k) {
        for (var a = Object.keys(k),
                c = 0; c < a.length; c++) typeof k[a[c]] === _$_43fc[85] && (t[a[c]] = k[a[c]], t.extensions[a[c]] = k[a[c]], t.license && typeof t[a[c]].license == _$_43fc[85] && t[a[c]].license.call(t, t.license))
    };
    t.destroy = function(k, a) {
        k.spreadsheet && (a && ka.unbind(k), a = t.spreadsheet.indexOf(k.spreadsheet), t.spreadsheet.splice(a, 1), k.spreadsheet = null, k.jexcel = null, k.innerHTML = _$_43fc[3], k.classList.remove(_$_43fc[571]), k.classList.remove(_$_43fc[349]))
    };
    t.version = F;
    t.helpers = A;
    t.events = xa;
    typeof formula !== _$_43fc[13] && (t.formula = formula);
    t.editors = function() {
        var k = {},
            a = function(c, b) {
                return 1 == c[b] || c.options && 1 == c.options[b] ? !0 : c[b] == _$_43fc[229] || c.options && c.options[b] == _$_43fc[229] ? !0 : !1
            };
        k.createEditor = function(c, b, d, e) {
            e.parent.input.setAttribute(_$_43fc[100], !1);
            c == _$_43fc[220] ? (c = document.createElement(_$_43fc[220]), c.type = _$_43fc[56], c.value = d) : c = document.createElement(_$_43fc[20]);
            c.style.width = b.offsetWidth - 2 + _$_43fc[44];
            c.style.height = b.offsetHeight - 1 + _$_43fc[44];
            e.parent.input.appendChild(c);
            return c
        };
        k.createDialog = function(c,
            b, d, e, f, g) {
            c = k.createEditor(_$_43fc[20], c, b, f);
            c.classList.add(_$_43fc[572]);
            c.classList.add(_$_43fc[566]);
            800 > window.innerWidth ? (f.parent.input.style.top = _$_43fc[144], f.parent.input.style.left = _$_43fc[144]) : (b = f.parent.input.getBoundingClientRect(), window.innerHeight < b.bottom && (f.parent.input.style.marginTop = -f.parent.input.offsetHeight));
            return c
        };
        k.general = function() {
            var c = {},
                b = function(e) {
                    e = (_$_43fc[3] + e)[0];
                    return e == _$_43fc[12] || e == _$_43fc[573] ? !0 : !1
                },
                d = function(e) {
                    if (e.format || e.mask || e.locale) {
                        var f = {};
                        e.mask ? f.mask = e.mask : e.format ? f.mask = e.format : (f.locale = e.locale, f.options = e.options);
                        e.decimal && (f.options || (f.options = {}), f.options = {
                            decimal: e.decimal
                        });
                        return f
                    }
                    return null
                };
            c.updateCell = function(e, f, g, h, l, n) {
                g = parseInt(g);
                h = parseInt(h);
                if (e) {
                    var q = d(n),
                        p = null;
                    f === _$_43fc[3] || b(f) || typeof f === _$_43fc[14] || (q ? (q = jSuites.mask.extract(f, q, !0)) && q.value !== _$_43fc[3] && (p = q.value) : 1 == (!1 === l.options.autoCasting || !1 === n.autoCasting ? !1 : !0) && (n.type == _$_43fc[14] || n.type == _$_43fc[307] || n.type == _$_43fc[306] ?
                        (q = jSuites.mask.extract(f, n, !0)) && q.value !== _$_43fc[3] && (p = q.value) : jSuites.isNumeric(Number(f)) && (p = Number(f))));
                    null !== p && (f = p);
                    f = c.parseValue(g, h, f, l, n, e);
                    f instanceof Element || f instanceof HTMLDocument ? (e.innerHTML = _$_43fc[3], e.appendChild(f)) : 1 == l.parent.config.stripHTML ? e.innerText = f : e.innerHTML = f;
                    if (null !== p) return p
                }
            };
            c.createCell = c.updateCell;
            c.openEditor = function(e, f, g, h, l, n) {
                var q = l.parent.input;
                q.onblur = function() {
                    q.classList.contains(_$_43fc[92]) || l.closeEditor(e, !0)
                };
                if (n.type == _$_43fc[14] ||
                    n.type == _$_43fc[307] || n.type == _$_43fc[306]) q.classList.add(_$_43fc[558]), b(f) || n.inputmode || (n.inputmode = _$_43fc[574]);
                g = null;
                b(f) || (n.inputmode && q.setAttribute(_$_43fc[96], n.inputmode), (g = d(n)) ? (n.disabledMaskOnEdition || (n.mask ? (n = n.mask.split(_$_43fc[263]), q.setAttribute(_$_43fc[94], n[0])) : n.locale && q.setAttribute(_$_43fc[575], n.locale)), g.input = l.parent.input, l.parent.input.mask = g, jSuites.mask.render(f, g, !1)) : f && n.type == _$_43fc[306] && (f *= 100));
                g || (q.innerText = f, jSuites.focus(l.parent.input))
            };
            c.closeEditor = function(e, f, g, h, l, n) {
                e = l.parent.input;
                f = f ? e.innerText : _$_43fc[3];
                b(f) ? f = f.replace(/(\r\n|\n|\r)/gm, _$_43fc[3]) : n.type == _$_43fc[306] && (f += _$_43fc[576], e.classList.remove(_$_43fc[577]));
                return f
            };
            c.parseValue = function(e, f, g, h, l, n) {
                b(g) && 1 == h.parent.config.parseFormulas && (g = h.executeFormula(g, e, f));
                g instanceof Element || g instanceof HTMLDocument || !l || b(g) || !(f = d(l)) || (e = jSuites.mask.render(g, f, !0), n && f.mask && (f = f.mask.split(_$_43fc[263]), f[1] && (f[1].match(new RegExp(_$_43fc[578], _$_43fc[377])) &&
                    (0 > g ? n.classList.add(_$_43fc[579]) : n.classList.remove(_$_43fc[579])), f[1].match(new RegExp(_$_43fc[580], _$_43fc[377])) && 0 > g && (e = _$_43fc[212] + e + _$_43fc[232]))), e && (g = e));
                return g
            };
            c[_$_43fc[51]] = function(e, f) {
                var g;
                return (g = d(e)) ? jSuites.mask.render(f, g, !0) : e.type == _$_43fc[306] ? 100 * parseFloat(f) : f
            };
            return c
        }();
        k.text = k.general;
        k.number = k.general;
        k.numeric = k.general;
        k.percent = k.general;
        k.notes = function() {
            var c = {},
                b = null;
            c.updateCell = function(d, e, f, g, h, l) {
                d && (f = document.createElement(_$_43fc[20]), f.classList.add(_$_43fc[581]),
                    1 == h.parent.config.stripHTML ? f.innerText = e : f.innerHTML = e, d.innerHTML = _$_43fc[3], d.appendChild(f))
            };
            c.createCell = c.updateCell;
            c.openEditor = function(d, e, f, g, h, l) {
                var n = l && l.options ? l.options : {};
                n.focus = !0;
                n.value = e;
                n.border = !1;
                n.height = _$_43fc[582];
                n.toolbar = !1;
                b = k.createDialog(d, e, f, g, h, l);
                b = jSuites.editor(b, n)
            };
            c.closeEditor = function(d, e, f, g, h, l) {
                return e ? b.getData() : _$_43fc[3]
            };
            return c
        }();
        k.dropdown = function() {
            var c = {
                createCell: function(b, d, e, f, g, h) {
                    h.render == _$_43fc[583] ? b.classList.add(_$_43fc[584]) :
                        b.classList.add(_$_43fc[585]);
                    if (b = c.updateCell(b, d, e, f, g, h)) return b
                },
                destroyCell: function(b) {
                    b.classList.remove(_$_43fc[585])
                },
                updateCell: function(b, d, e, f, g, h) {
                    if (b) {
                        var l = c.getItem(b, d, e, f, g, h);
                        l ? b.innerHTML = l : d ? (d = c.fromLabel(b, d, e, f, g, h), d.length ? (l = c.getItem(b, d, e, f, g, h), b.innerHTML = l) : b.innerHTML = _$_43fc[3]) : b.innerHTML = _$_43fc[3];
                        return d
                    }
                },
                openEditor: function(b, d, e, f, g, h) {
                    var l = k.createEditor(_$_43fc[20], b, d, g),
                        n = h.delimiter || _$_43fc[263];
                    h = Object.create(h);
                    h.options || (h.options = {});
                    typeof h.filter ==
                        _$_43fc[85] && (h.source = h.filter(g.el, b, e, f, h.source));
                    typeof h.filterOptions == _$_43fc[85] && (h = h.filterOptions(g, b, e, f, d, h));
                    e = h.options;
                    h.source && (e.data = JSON.parse(JSON.stringify(h.source)));
                    a(h, _$_43fc[586]) && (e.multiple = !0);
                    a(h, _$_43fc[57]) && (e.autocomplete = !0);
                    e.format = !0;
                    e.opened = !0;
                    e.width = b.offsetWidth - 2;
                    e.onclose = function() {
                        g.closeEditor(b, !0)
                    };
                    a(h, _$_43fc[586]) ? d && (e.value = (_$_43fc[3] + d).split(n)) : e.value = d;
                    jSuites.dropdown(l, e)
                },
                closeEditor: function(b, d, e, f, g, h) {
                    for (var l = g.parent.input.children[0],
                            n = h.delimiter || _$_43fc[263], q = l.dropdown.getText(!0), p = l.dropdown.getValue(!0), r = 0; r < p.length; r++) !1 === c.getItem(b, p[r], e, f, g, h) && h.source.push({
                        id: p[r],
                        name: q[r]
                    });
                    l.dropdown.close(!0);
                    if (d) return p.join(n)
                },
                fromLabel: function(b, d, e, f, g, h) {
                    b = h.delimiter || _$_43fc[263];
                    e = [];
                    f = [];
                    Array.isArray(d) || (d = (_$_43fc[3] + d).split(b));
                    for (g = 0; g < d.length; g++) f[d[g].trim()] = !0;
                    d = Object.keys(f);
                    !a(h, _$_43fc[586]) && 1 < Object.keys(d).length && (f = [], f[d[0]] = !0);
                    for (d = 0; d < h.source.length; d++) f[h.source[d].name] && e.push(h.source[d].id);
                    return e.join(b)
                },
                getItem: function(b, d, e, f, g, h) {
                    f = h.delimiter || _$_43fc[263];
                    b = [];
                    e = [];
                    Array.isArray(d) || (d = (_$_43fc[3] + d).split(f));
                    for (f = 0; f < d.length; f++) e[d[f].trim()] = !0;
                    f = Object.keys(e);
                    !a(h, _$_43fc[586]) && 1 < Object.keys(f).length && (d = [], d[f[0]] = !0, e = d);
                    if (h.source && typeof h.source[0] !== _$_43fc[84])
                        for (d = 0; d < h.source.length; d++) f = h.source[d], h.source[d] = {
                            id: f,
                            name: f
                        };
                    for (d = 0; d < h.source.length; d++) typeof e[h.source[d].id] !== _$_43fc[13] && b.push(h.render == _$_43fc[255] ? h.source[d].color : h.render ==
                        _$_43fc[587] ? h.source[d].image : h.source[d].name);
                    if (b.length) {
                        if (typeof h.render !== _$_43fc[13]) {
                            for (d = 0; d < b.length; d++) e = d, f = b[d], f = h.render == _$_43fc[255] ? _$_43fc[588] + f + _$_43fc[589] : h.render == _$_43fc[587] ? _$_43fc[588] + f + _$_43fc[589] : h.render == _$_43fc[583] ? _$_43fc[591] + (h.source[d].color || _$_43fc[590]) + _$_43fc[592] + f + _$_43fc[593] : void 0, b[e] = f;
                            return b.join(_$_43fc[3])
                        }
                        for (d = 0; d < b.length; d++) b[d] = (_$_43fc[3] + b[d]).replace(_$_43fc[215], _$_43fc[594]);
                        return b.join(_$_43fc[295])
                    }
                    return !1
                }
            };
            c[_$_43fc[51]] =
                function(b, d) {
                    for (var e = 0; e < b.source.length; e++)
                        if (b.source[e].id == d) return b.render == _$_43fc[255] ? b.source[e].color : b.render == _$_43fc[587] ? b.source[e].image : b.source[e].name
                };
            return c
        }();
        k.autocomplete = k.dropdown;
        k.calendar = function() {
            var c = {},
                b = null;
            c.updateCell = function(d, e, f, g, h, l) {
                if (d) {
                    f = e;
                    l = c.getFormat(l);
                    0 < e && Number(e) == e && (f = jSuites.calendar.numToDate(e));
                    g = _$_43fc[3] + e;
                    g.substr(4, 1) == _$_43fc[185] && g.substr(7, 1) == _$_43fc[185] ? g = !0 : (g = g.split(_$_43fc[185]), g = 4 == g[0].length && g[0] == Number(g[0]) &&
                        2 == g[1].length && g[1] == Number(g[1]) ? !0 : !1);
                    g || (g = jSuites.calendar.extractDateFromString(e, l)) && (f = g);
                    if (f = jSuites.calendar.getDateString(f, l)) return d.innerText = f, e;
                    d.innerText = _$_43fc[3];
                    return _$_43fc[3]
                }
            };
            c.createCell = c.updateCell;
            c.openEditor = function(d, e, f, g, h, l) {
                l = Object.create(l);
                typeof l.filterOptions == _$_43fc[85] && (l = l.filterOptions(h, d, f, g, e, l));
                f = l.options || {};
                f.opened = !0;
                f.onclose = function(n, q) {
                    h.closeEditor(d, !0)
                };
                typeof l.timepicker !== _$_43fc[13] && (f.time = a(l, _$_43fc[595]) ? !0 : !1);
                typeof f.readonly !==
                    _$_43fc[13] && (f.readonly = f.readonly);
                f.value = e || null;
                f.placeholder = f.format;
                b = k.createEditor(_$_43fc[220], d, e, h);
                jSuites.calendar(b, f);
                !1 === b.calendar.options.readonly && b.focus()
            };
            c.closeEditor = function(d, e, f, g, h) {
                d = b.calendar.close(!0, b.value ? !0 : !1);
                d = b.value ? d : _$_43fc[3];
                if (e) return d
            };
            c.getFormat = function(d) {
                return d && d.format ? d.format : d && d.options && d.options.format ? d.options.format : _$_43fc[596]
            };
            c[_$_43fc[51]] = function(d, e) {
                d = c.getFormat(d);
                return jSuites.calendar.getDateString(e, d)
            };
            return c
        }();
        k.color = function() {
            var c = {},
                b = null;
            c.updateCell = function(d, e, f, g, h, l) {
                d && (l.render == _$_43fc[597] ? (f = document.createElement(_$_43fc[20]), f.className = _$_43fc[255], f.style.backgroundColor = e, d.innerText = _$_43fc[3], d.appendChild(f)) : (d.style.color = e, d.innerText = e))
            };
            c.createCell = c.updateCell;
            c.openEditor = function(d, e, f, g, h, l) {
                l = Object.create(l);
                typeof l.filterOptions == _$_43fc[85] && (l = l.filterOptions(h, d, f, g, e, l));
                l.value = e;
                l.onclose = function(n, q) {
                    h.closeEditor(d, !0)
                };
                b = k.createEditor(_$_43fc[220], d, e,
                    h);
                b = jSuites.color(b, l);
                b.open()
            };
            c.closeEditor = function(d, e, f, g, h, l) {
                d = b.close(!0);
                if (e) return d
            };
            c[_$_43fc[51]] = function(d, e) {
                d = document.createElement(_$_43fc[20]);
                d.style.width = _$_43fc[598];
                d.style.height = _$_43fc[22];
                d.style.backgroundColor = e;
                return d.outerHTML
            };
            return c
        }();
        k.checkbox = function() {
            var c = {
                createCell: function(b, d, e, f, g, h) {
                    d = d && d != _$_43fc[599] && d != _$_43fc[600] ? !0 : !1;
                    e = document.createElement(_$_43fc[220]);
                    e.type = _$_43fc[250];
                    e.checked = d;
                    e.onclick = function() {
                        g.setValue(b, this.checked)
                    };
                    (h && 1 == h.readOnly || !g.isEditable()) && e.setAttribute(_$_43fc[601], _$_43fc[601]);
                    b.innerHTML = _$_43fc[3];
                    b.appendChild(e);
                    return d
                },
                updateCell: function(b, d) {
                    d = d && d != _$_43fc[599] && d != _$_43fc[600] ? !0 : !1;
                    b && (b.children[0].checked = d);
                    return d
                },
                openEditor: function(b, d, e, f, g) {
                    d = b.children[0].checked ? !1 : !0;
                    g.setValue(b, d);
                    return !1
                },
                closeEditor: function(b, d) {
                    return !1
                }
            };
            c[_$_43fc[51]] = function(b, d) {
                return 1 == d || 1 == d || d == _$_43fc[229] ? jSuites.translate(_$_43fc[229]) : jSuites.translate(_$_43fc[599])
            };
            return c
        }();
        k.radio = function() {
            var c = {
                createCell: function(b, d, e, f, g, h) {
                    b.getAttribute(_$_43fc[42]);
                    e = document.createElement(_$_43fc[220]);
                    e.type = _$_43fc[252];
                    e.checked = 1 == d || 1 == d || d == _$_43fc[229] ? !0 : !1;
                    e.onclick = function() {
                        g.setValue(b, !0)
                    };
                    (h && 1 == h.readOnly || !g.isEditable()) && e.setAttribute(_$_43fc[601], _$_43fc[601]);
                    b.innerHTML = _$_43fc[3];
                    b.appendChild(e)
                },
                updateCell: function(b, d, e, f, g) {
                    d = 1 == d || 1 == d || d == _$_43fc[229] || d == _$_43fc[602] ? !0 : !1;
                    b && (b.children[0].checked = d);
                    if (1 == d) {
                        b = [];
                        for (var h = 0; h < g.options.data.length; h++) f !=
                            h && g.options.data[h][e] && b.push({
                                x: e,
                                y: h,
                                value: 0
                            });
                        b.length && g.setValue(b)
                    }
                    return d
                },
                openEditor: function(b, d, e, f, g) {
                    return !1
                },
                closeEditor: function(b, d) {
                    return !1
                }
            };
            c[_$_43fc[51]] = function(b, d) {
                return 1 == d || 1 == d || d == _$_43fc[229] ? _$_43fc[229] : _$_43fc[599]
            };
            return c
        }();
        k.autonumber = function() {
            var c = {},
                b = null;
            c.createCell = function(d, e, f, g, h, l) {
                (e = parseInt(e)) || 0 < parseInt(d.innerText) && (e = parseInt(d.innerText));
                l.sequence || (l.sequence = 0);
                e ? c.isValid(e, f, g, h) || (e = _$_43fc[243]) : e = l.sequence + 1;
                e > l.sequence &&
                    (l.sequence = e);
                return d.innerText = e
            };
            c.updateCell = c.createCell;
            c.openEditor = function(d, e, f, g, h, l) {
                b = k.createEditor(_$_43fc[220], d, e, h);
                b.onblur = function() {
                    h.closeEditor(d, !0)
                };
                b.focus();
                b.value = e
            };
            c.closeEditor = function(d, e, f, g, h) {
                return e ? b.value : _$_43fc[3]
            };
            c.isValid = function(d, e, f, g) {
                for (var h, l = 0; l < g.options.data.length; l++)
                    if (h = g.value(e, l), h == d && l != f) return !1;
                return !0
            };
            return c
        }();
        k.progressbar = function() {
            var c = {},
                b = null;
            c.createCell = function(d, e) {
                e = parseInt(e);
                if (100 < e) e = 100;
                else if (!e || 0 >
                    e) e = 0;
                if (d.children[0] && d.children[0].tagName == _$_43fc[555]) var f = d.children[0];
                else f = document.createElement(_$_43fc[20]), f.classList.add(_$_43fc[256]), d.innerText = _$_43fc[3], d.classList.add(_$_43fc[603]), d.appendChild(f);
                f.style.width = parseInt(e) + _$_43fc[576];
                f.setAttribute(_$_43fc[264], parseInt(e) + _$_43fc[576])
            };
            c.destroyCell = function(d) {
                d.classList.remove(_$_43fc[603])
            };
            c.updateCell = c.createCell;
            c.openEditor = function(d, e, f, g, h, l) {
                b = k.createEditor(_$_43fc[220], d, e, h);
                b.type = _$_43fc[14];
                b.setAttribute(_$_43fc[604],
                    0);
                b.setAttribute(_$_43fc[605], 100);
                b.onblur = function() {
                    h.closeEditor(d, !0)
                };
                b.focus();
                b.value = e
            };
            c.closeEditor = function(d, e, f, g, h) {
                return e ? b.value : _$_43fc[3]
            };
            return c
        }();
        k.rating = function() {
            var c = {},
                b = null;
            c.createCell = function(d, e, f, g, h, l) {
                return c.setCell(d, e, l)
            };
            c.destroyCell = function(d) {
                d.classList.remove(_$_43fc[606])
            };
            c.updateCell = c.createCell;
            c.openEditor = function(d, e, f, g, h, l) {
                b = k.createEditor(_$_43fc[220], d, e, h);
                b.type = _$_43fc[14];
                b.setAttribute(_$_43fc[604], 0);
                b.setAttribute(_$_43fc[605],
                    5);
                b.onblur = function() {
                    h.closeEditor(d, !0)
                };
                b.focus();
                b.value = e
            };
            c.closeEditor = function(d, e, f, g, h, l) {
                return e ? b.value : _$_43fc[3]
            };
            c.setCell = function(d, e, f) {
                e = parseInt(e);
                if (5 < e) e = 5;
                else if (!e || 0 > e) e = 0;
                if (d) {
                    f = f && f.color ? f.color : _$_43fc[579];
                    var g = document.createElement(_$_43fc[20]);
                    g.setAttribute(_$_43fc[264], parseInt(e) + _$_43fc[607]);
                    g.classList.add(_$_43fc[257]);
                    for (var h = 0; h < e; h++) {
                        var l = document.createElement(_$_43fc[443]);
                        l.className = _$_43fc[508];
                        l.style.color = f;
                        l.innerHTML = _$_43fc[608];
                        g.appendChild(l)
                    }
                    d.innerHTML =
                        _$_43fc[3];
                    d.className = _$_43fc[606];
                    d.appendChild(g)
                }
                return e
            };
            return c
        }();
        k.email = function() {
            var c = {
                createCell: function(b, d, e, f, g, h) {
                    b && (b.children && b.children[0] && b.children[0].tagName == _$_43fc[562] ? e = b.children[0] : (e = document.createElement(_$_43fc[201]), b.innerText = _$_43fc[3], b.appendChild(e), h.options && h.options.url && e.setAttribute(_$_43fc[609], h.options.url)), e.innerText = d)
                }
            };
            c.updateCell = c.createCell;
            c.openEditor = function(b, d, e, f, g, h) {
                g.parent.input.classList.add(_$_43fc[558]);
                g.parent.input.onblur =
                    function() {
                        g.closeEditor(b, !0)
                    };
                g.parent.input.innerText = d;
                jSuites.focus(g.parent.input)
            };
            c.closeEditor = function(b, d, e, f, g) {
                g.parent.input.classList.remove(_$_43fc[558]);
                return d ? g.parent.input.innerText.replace(new RegExp(/\n/, _$_43fc[188]), _$_43fc[3]) : _$_43fc[3]
            };
            return c
        }();
        k.url = k.email;
        k.image = function() {
            var c = {},
                b = null;
            c.createCell = function(d, e, f, g, h, l) {
                d && (e ? d.children && d.children[0] && d.children[0].tagName == _$_43fc[610] ? d.children[0].src = e : (f = document.createElement(_$_43fc[37]), l.render == _$_43fc[611] &&
                    f.classList.add(_$_43fc[611]), l.options && (f.style.maxWidth = _$_43fc[612], l.options.absolute && (f.style.position = _$_43fc[613]), l.options.width && (f.style.maxWidth = parseInt(l.options.width) + _$_43fc[44]), l.options.height && (f.style.maxHeight = parseInt(l.options.height) + _$_43fc[44], f.style.marginTop = -1 * parseInt(l.options.height) / 2 + _$_43fc[44])), f.src = e, d.innerHTML = _$_43fc[3], d.appendChild(f)) : d.innerHTML = _$_43fc[3])
            };
            c.updateCell = c.createCell;
            c.openEditor = function(d, e, f, g, h, l) {
                (l && l.options ? l.options : {}).value =
                    e;
                b = k.createDialog(d, e, f, g, h, l);
                e && (d = document.createElement(_$_43fc[37]), d.src = e, d.classList.add(_$_43fc[614]), d.style.width = _$_43fc[612], b.appendChild(d));
                jSuites.image(b, l)
            };
            c.closeEditor = function(d, e, f, g, h, l) {
                if (e && (d = b.children[0])) return d.tagName == _$_43fc[610] ? d.src : _$_43fc[3]
            };
            c[_$_43fc[51]] = function(d, e) {
                return _$_43fc[615] + e + _$_43fc[616]
            };
            return c
        }();
        k.html = function() {
            var c = {},
                b = null;
            c.createCell = function(d, e) {
                d.classList.add(_$_43fc[617]);
                var f = document.createElement(_$_43fc[20]);
                f.innerHTML =
                    e;
                d.appendChild(f)
            };
            c.updateCell = function(d, e) {
                d && (d.firstChild.innerHTML = e)
            };
            c.destroyCell = function(d) {
                d.classList.remove(_$_43fc[617])
            };
            c.openEditor = function(d, e, f, g, h, l) {
                var n = l && l.options ? l.options : {};
                n.focus = !0;
                n.value = e;
                n.border = !1;
                n.height = _$_43fc[582];
                e = k.createDialog(d, e, f, g, h, l);
                b = jSuites.editor(e, n);
                b.close = function() {
                    h.closeEditor(d, !0)
                };
                jSuites.tracking(b, !0)
            };
            c.closeEditor = function(d, e, f, g, h, l) {
                jSuites.tracking(b, !1);
                d = b.getData();
                if (e) return d
            };
            return c
        }();
        k.hidden = function() {
            return {
                createCell: function(c,
                    b, d, e, f) {
                    c.style.display = _$_43fc[40];
                    c.innerText = b
                },
                updateCell: function(c, b, d, e, f) {
                    c && (c.innerText = b)
                },
                openEditor: function(c, b, d, e, f) {
                    return !1
                },
                closeEditor: function(c, b) {
                    return !1
                }
            }
        }();
        k.tags = function() {
            var c = {},
                b = null;
            c.createCell = function(d, e) {
                d.classList.add(_$_43fc[618]);
                c.updateCell(d, e)
            };
            c.updateCell = function(d, e) {
                d && (d.innerHTML = e)
            };
            c.destroyCell = function(d) {
                d.classList.remove(_$_43fc[618])
            };
            c.openEditor = function(d, e, f, g, h, l) {
                var n = l && l.options ? l.options : {};
                n.value = e;
                b = k.createDialog(d, e, f,
                    g, h, l);
                d = document.createElement(_$_43fc[20]);
                d.style.margin = _$_43fc[22];
                d.style.marginRight = _$_43fc[619];
                b.appendChild(d);
                b = jSuites.tags(d, n);
                d.focus();
                jSuites.focus(d)
            };
            c.closeEditor = function(d, e, f, g, h, l) {
                d = b.getValue();
                if (e) return d
            };
            return c
        }();
        k.record = function() {
            var c = {},
                b = null;
            c.createCell = function(d, e, f, g, h, l) {
                d.classList.add(_$_43fc[585]);
                var n;
                (n = h.parent.getWorksheetInstance(l.worksheetId)) ? n.rows ? c.updateCell(d, e, f, g, h, l) : (d.innerText = _$_43fc[243], h.parent.queue.push([h, f, g])): (d.innerText =
                    _$_43fc[243], h.parent.queue.push([h, f, g]))
            };
            c.destroyCell = function(d) {
                d.classList.remove(_$_43fc[585])
            };
            c.updateCell = function(d, e, f, g, h, l) {
                d && (typeof l.worksheetId == _$_43fc[13] ? d.innerText = _$_43fc[243] : d.innerHTML = c.getValue(d, e, f, g, h, l))
            };
            c.openEditor = function(d, e, f, g, h, l) {
                var n = l.delimiter || _$_43fc[263];
                l = Object.create(l);
                l.options || (l.options = {});
                typeof l.filter == _$_43fc[85] && (l.source = l.filter(h.el, d, f, g, l.source));
                typeof l.filterOptions == _$_43fc[85] && (l = l.filterOptions(h, d, f, g, e, l));
                f = l.options;
                f.data = c.getSource(h, l);
                a(l, _$_43fc[586]) && (f.multiple = !0);
                a(l, _$_43fc[57]) && (f.autocomplete = !0);
                f.opened = !0;
                f.width = d.offsetWidth - 2;
                f.onclose = function() {
                    h.closeEditor(d, !0)
                };
                a(l, _$_43fc[586]) ? e && (f.value = (_$_43fc[3] + e).split(n)) : f.value = e;
                b = k.createEditor(_$_43fc[20], d, e, h);
                b = jSuites.dropdown(b, f)
            };
            c.closeEditor = function(d, e, f, g, h, l) {
                d = l.delimiter || _$_43fc[263];
                f = b.getValue(!0);
                b.close(!0);
                if (e) return f.join(d)
            };
            c.getValue = function(d, e, f, g, h, l) {
                d = [];
                if (typeof l.worksheetId == _$_43fc[17] && (h = h.parent.getWorksheetInstance(l.worksheetId))) {
                    f =
                        l.worksheetColumn ? l.worksheetColumn : 0;
                    0 <= l.worksheetImage && (f = l.worksheetImage);
                    if (e == _$_43fc[3] || 0 == e || null == e || void 0 == e) return _$_43fc[3];
                    e = (_$_43fc[3] + e).split(_$_43fc[263]);
                    for (g = 0; g < e.length; g++) {
                        var n = e[g].trim();
                        var q = h.name(f);
                        n = h.getRowById(n);
                        q = !1 === n ? _$_43fc[620] : n[q];
                        0 <= l.worksheetImage && (q = _$_43fc[621] + q + _$_43fc[622]);
                        d.push(q)
                    }
                    return 0 <= l.worksheetImage ? d.join(_$_43fc[3]) : d.join(_$_43fc[295])
                }
                return _$_43fc[243]
            };
            c.getSource = function(d, e) {
                var f = e.worksheetColumn ? e.worksheetColumn : 0,
                    g = null;
                typeof e.worksheetId == _$_43fc[17] && (g = d.parent.getWorksheetInstance(e.worksheetId));
                if (g) {
                    var h, l = [];
                    for (d = 0; d < g.rows.length; d++)
                        if (h = g.value(f, d)) h = {
                            id: g.getRowId(d),
                            name: h
                        }, void 0 != e.worksheetImage && (h.image = g.value(e.worksheetImage, d)), l.push(h)
                } else console.error(_$_43fc[623] + e.worksheetId);
                return l
            };
            c[_$_43fc[51]] = function(d, e) {
                for (var f = 0; f < d.source.length; f++)
                    if (d.source[f].id == e) return d.render == _$_43fc[255] ? d.source[f].color : d.render == _$_43fc[587] ? d.source[f].image : d.source[f].name
            };
            return c
        }();
        return k
    }();
    typeof jQuery !== _$_43fc[13] && function(k) {
        k.fn.jspreadsheet = k.fn.jexcel = function(a) {
            var c = k(this)[_$_43fc[51]](0);
            if (c.jspreadsheet) {
                if (typeof arguments[0] == _$_43fc[14]) var b = arguments[0],
                    d = 2;
                else b = 0, d = 1;
                return c.jspreadsheet[b][a].apply(c.jspreadsheet[b], Array.prototype.slice.call(arguments, d))
            }
            return t(k(this)[_$_43fc[51]](0), arguments[0])
        }
    }(jQuery);
    return t
});