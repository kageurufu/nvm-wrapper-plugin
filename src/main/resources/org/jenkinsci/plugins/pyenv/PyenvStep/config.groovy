package org.jenkinsci.plugins.pyenv.PyenvStep

import lib.LayoutTagLib


l = namespace(LayoutTagLib)
t = namespace('/lib/hudson')
st = namespace('jelly:stapler')
f = namespace('/lib/form')

f.entry(title: 'Version', field: 'version') {
  f.textbox()
}

f.entry(title:"Pyenv Install URL", field:"pyenvInstallURL") {
  f.textbox()
}
