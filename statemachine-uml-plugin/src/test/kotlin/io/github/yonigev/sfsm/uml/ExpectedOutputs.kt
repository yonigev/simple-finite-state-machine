package io.github.yonigev.sfsm.uml


const val SIMPLE_SM_UML = """
digraph StateMachine {
graph [label="TEST", fontsize=16]
// Define states
START [shape="doublecircle" style="rounded,filled" fillcolor="aliceblue"] 
S1 [shape="box" style="rounded,filled" fillcolor="aliceblue"] 
S2 [shape="box" style="rounded,filled" fillcolor="aliceblue"] 
S3 [shape="diamond" style="rounded,filled" fillcolor="aliceblue"] 
END1 [shape="doublecircle" style="rounded,filled" fillcolor="aliceblue"] 
END2 [shape="doublecircle" style="rounded,filled" fillcolor="aliceblue"]
// Define transitions between states
START -> S1 [label="GOTO_S1"] 
S1 -> S2 [label="GOTO_S2"] 
S2 -> S3 [label="GOTO_S3"] 
S3 -> END1 [label="GOTO_END1"] 
S3 -> END2 [label="GOTO_END2"]
}
"""
const val DETAILED_SM_UML_WITH_ACTIONS = """
digraph StateMachine {
rankdir=LR;
graph [label="TEST", fontsize=16]
// Define states
START [shape="doublecircle" label="START" style="rounded,filled" fillcolor="aliceblue" fontname ="times"] 
subgraph cluster_S1  {
label="S1";
style=solid;
bgcolor=aliceblue;
S1_MyEntryAction [shape=circle, style=filled, color=black, label="", width=0.1]
S1_MyEntryAction [shape="cds" label="S1_MyEntryAction" style="rounded,filled" fillcolor="thistle" fontname ="times"]
S1 [style="rounded,filled" label="S1" fillcolor="aliceblue" fontname ="times"]
S1_MyExitAction [shape=circle, style=filled, color=black, label="", width=0.1]
S1_MyExitAction [shape="cds" label="S1_MyExitAction" style="rounded,filled" fillcolor="thistle" fontname ="times"]        
S1_MyEntryAction -> S1_MyEntryAction [label="" style="invis" arrowhead="normal" fontname="times" ]
S1_MyEntryAction -> S1 [label=""  arrowhead="normal" fontname="times" ]
S1 -> S1_MyExitAction [label=""  arrowhead="normal" fontname="times" ]
S1_MyExitAction -> S1_MyExitAction [label="" style="invis" arrowhead="normal" fontname="times" ]
      } 
S2 [shape="box" label="S2" style="rounded,filled" fillcolor="aliceblue" fontname ="times"] 
subgraph cluster_S3  {
label="S3";
style=solid;
bgcolor=aliceblue;
S3_MyEntryAction [shape=circle, style=filled, color=black, label="", width=0.1]
S3_MyEntryAction [shape="cds" label="S3_MyEntryAction" style="rounded,filled" fillcolor="thistle" fontname ="times"]
S3 [style="rounded,filled" label="S3" fillcolor="aliceblue" fontname ="times"]
S3_MyExitAction [shape=circle, style=filled, color=black, label="", width=0.1]
S3_MyExitAction [shape="cds" label="S3_MyExitAction" style="rounded,filled" fillcolor="thistle" fontname ="times"]        
S3_MyEntryAction -> S3_MyEntryAction [label="" style="invis" arrowhead="normal" fontname="times" ]
S3_MyEntryAction -> S3 [label=""  arrowhead="normal" fontname="times" ]
S3 -> S3_MyExitAction [label=""  arrowhead="normal" fontname="times" ]
S3_MyExitAction -> S3_MyExitAction [label="" style="invis" arrowhead="normal" fontname="times" ]
      } 
END1 [shape="doublecircle" label="END1" style="rounded,filled" fillcolor="aliceblue" fontname ="times"] 
END2 [shape="doublecircle" label="END2" style="rounded,filled" fillcolor="aliceblue" fontname ="times"]
// Define transitions between states
 START_GOTO_S1_S1_ofPredicate1 [shape="diamond" label="ofPredicate1" style="rounded,filled" fillcolor="seashell2" fontname ="times"]
START -> START_GOTO_S1_S1_ofPredicate1 [label="GOTO_S1"  arrowhead="none" fontname="times" ]
START_GOTO_S1_S1_ofPredicate1 -> S1_MyEntryAction [label=""  arrowhead="normal" fontname="times" ]
S1_GOTO_S2_S2_MyGuard [shape="diamond" label="MyGuard" style="rounded,filled" fillcolor="seashell2" fontname ="times"]
S1_MyExitAction -> S1_GOTO_S2_S2_MyGuard [label="GOTO_S2"  arrowhead="none" fontname="times" ]
S1_GOTO_S2_S2_MyGuard -> S2 [label=""  arrowhead="normal" fontname="times" ]
S2_GOTO_S3_S3_ofPredicate1 [shape="diamond" label="ofPredicate1" style="rounded,filled" fillcolor="seashell2" fontname ="times"]
S2 -> S2_GOTO_S3_S3_ofPredicate1 [label="GOTO_S3"  arrowhead="none" fontname="times" ]
S2_GOTO_S3_S3_ofPredicate1 -> S3_MyEntryAction [label=""  arrowhead="normal" fontname="times" ]
S3_GOTO_END1_END1_ofPredicate1 [shape="diamond" label="ofPredicate1" style="rounded,filled" fillcolor="seashell2" fontname ="times"]
S3_MyExitAction -> S3_GOTO_END1_END1_ofPredicate1 [label=< <table border="0" cellborder="0" cellspacing="0" cellpadding="4" font="times">
<tr><td bgcolor="white" colspan="1"> <table border="0" cellborder="1" cellspacing="0" cellpadding="4" font="times">
<tr><td bgcolor="thistle" colspan="1">MyTransitionAction</td></tr>
<tr><td bgcolor="thistle" colspan="1">MyTransitionAction</td></tr>
</table></td></tr>
<tr><td bgcolor="white" colspan="1"> <table border="0" cellborder="0" cellspacing="0" cellpadding="4" font="times">
<tr><td bgcolor="white" colspan="1">GOTO_END1</td></tr>
</table></td></tr>
</table>>  arrowhead="none" fontname="times" ]
S3_GOTO_END1_END1_ofPredicate1 -> END1 [label=""  arrowhead="normal" fontname="times" ]
S3_GOTO_END2_END2_ofPredicate1 [shape="diamond" label="ofPredicate1" style="rounded,filled" fillcolor="seashell2" fontname ="times"]
S3_MyExitAction -> S3_GOTO_END2_END2_ofPredicate1 [label="GOTO_END2"  arrowhead="none" fontname="times" ]
S3_GOTO_END2_END2_ofPredicate1 -> END2 [label=""  arrowhead="normal" fontname="times" ]
Legend [shape="none" label=< <table border="0" cellborder="1" cellspacing="0" cellpadding="4" font="times">
<tr><td bgcolor="white" colspan="2">Legend</td></tr>
<tr><td bgcolor="white" colspan="1">States</td> <td bgcolor="aliceblue" colspan="1">         </td></tr>
<tr><td bgcolor="white" colspan="1">Guards</td> <td bgcolor="seashell2" colspan="1">         </td></tr>
<tr><td bgcolor="white" colspan="1">Actions</td> <td bgcolor="thistle" colspan="1">         </td></tr>
</table>> style="rounded,filled" fillcolor="aliceblue" fontname ="times"]
}
"""

const val ACTIONS_TABLE_UML = """
<<table border="0" cellborder="0" cellspacing="0" cellpadding="4" font="times">
<tr><td bgcolor="white" colspan="1"> <table border="0" cellborder="1" cellspacing="0" cellpadding="4" font="times">
<tr><td bgcolor="thistle" colspan="1">MyTransitionAction</td></tr>
<tr><td bgcolor="thistle" colspan="1">MyTransitionAction</td></tr>
</table></td></tr>
<tr><td bgcolor="white" colspan="1"> <table border="0" cellborder="0" cellspacing="0" cellpadding="4" font="times">
<tr><td bgcolor="white" colspan="1">GOTO_END1</td></tr>
</table></td></tr>
</table>>"""
