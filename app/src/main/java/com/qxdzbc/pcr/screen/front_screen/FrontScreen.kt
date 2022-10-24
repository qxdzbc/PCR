package com.qxdzbc.pcr.screen.front_screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.qxdzbc.pcr.screen.front_screen.state.FrontScreenState
import com.qxdzbc.pcr.screen.front_screen.state.FrontScreenStateImp
import com.qxdzbc.pcr.screen.main_screen.mainScreenNavTag
import com.qxdzbc.pcr.ui.theme.PCRTheme

const val frontScreenNavTag = "FrontScreen_NavTag"

@Composable
fun FrontScreen(
    state: FrontScreenState,
    action: FrontScreenAction,
    login: () -> Unit,
) {
    Surface {
        ConstraintLayout(
            modifier = Modifier.fillMaxSize(),
        ) {
            val (title, subTitle, loginBut, switch) = createRefs()
            Text(
                text = "PCR",
                fontSize = 60.sp,
//                color = MaterialTheme.colors.onSurface,
                fontWeight = FontWeight.W900,
                modifier = Modifier.constrainAs(title) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )
            Text(
                text = "personal cost recorder",
                fontSize = 15.sp,
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier.constrainAs(subTitle) {
                    top.linkTo(title.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )
            Button(
                onClick = {
                    login()
                },
                modifier = Modifier
                    .constrainAs(loginBut) {
                        top.linkTo(subTitle.bottom)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .width(150.dp)
            ) {
                Text("Login")
            }
            Row(
                modifier = Modifier.constrainAs(switch) {
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (state.isDark) "dark" else "light",
//                    color = MaterialTheme.colors.onSurface
                )
                Switch(
                    checked = state.isDark,
                    onCheckedChange = { newValue ->
                        action.switchTheme(newValue)
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colors.onSurface,
//                        uncheckedThumbColor = MaterialTheme.colors.onSurface,
                    ),
                )
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun FrontScreenPreview() {
    PCRTheme(darkTheme = true) {
        FrontScreen(
            state = FrontScreenStateImp.forTest(),
            action = FrontScreenAction.doNothing,
            login = {}
        )
    }
}