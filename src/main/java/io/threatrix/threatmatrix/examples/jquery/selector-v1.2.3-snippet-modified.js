else {
				re = /^([>+~])\s*(\w*)/i;

				if ( (m = re.exec(t)) != null ) {
					t = [];

					var merge = {};
					nodeName = m[2].toUpperCase();
					m = m[1];

					for ( var j = 0, rl = ret.length; j < rl; j++ ) {
						var n = m == "~" || m == "+" ? ret[j].nextSibling : ret[j].firstChild;
						for ( ; n; n = n.nextSibling )
							if ( n.nodeType == 1 ) {
								
								if ( m == "~" && merge[id] ) break;
								
								var nid = jQuery.data(n);
								
								if (!nodeName || n.nodeName.toUpperCase() == nodeName ) {
									t.push( n );
									if ( m == "~" ) merge[nid] = true;
									
								}
								
								if ( m == "+" ) continue;
							}
					}

					ret = g;

					// And remove the token
					q = jQuery.trim( t.replace( re, "" ) );
					foundToken = true;
				}
