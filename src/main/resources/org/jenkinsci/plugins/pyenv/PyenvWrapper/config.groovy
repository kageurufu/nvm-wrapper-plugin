import lib.LayoutTagLib

l=namespace(LayoutTagLib)
t=namespace("/lib/hudson")
st=namespace("jelly:stapler")
f=namespace("lib/form")

f.entry(title:"Version", field:"version") {
  f.textbox()
}

f.optionalBlock(title:"Pyenv Advanced Settings", inline:true){

  f.entry(title:"Pyenv Installer URL", field:"pyenvInstallURL") {
    f.textbox()
  }

}
