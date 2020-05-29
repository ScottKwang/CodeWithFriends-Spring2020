/**
 * @format
 * @flow strict-local
 */
import * as React from 'react';
import {SafeAreaView, View, Text, StatusBar, ScrollView} from 'react-native';
// @ts-ignore
import CustomDatePicker from '../../components/CustomDatePicker';
import {CheckBox, Input, Button, Icon} from 'react-native-elements';
import {colors} from '../../assets';
import {signup} from './styles';

export interface IProps {
  navigation: any;
}
export interface IState {}

class SignUpScreen extends React.Component<IProps, IState> {
  render() {
    return (
      <View style={signup.flex1Column}>
        <StatusBar barStyle="dark-content" backgroundColor={colors.ocean1} />
        <ScrollView style={signup.scrollViewBackgroundColor}>
          <SafeAreaView style={signup.safeAreaView} />
          <View style={signup.logoContainer}>
            <Icon
              raised
              name="tooth"
              size={45}
              type="material-community"
              color={colors.ocean1}
            />
            <Text style={signup.mainText}>Twinkle Teeth</Text>
          </View>
          <View style={signup.flex1}>
            <Text style={[signup.subText, signup.subTextStyle]}>Sign Up</Text>
          </View>
          <View style={signup.flex6}>
            <Input
              placeholder="First name"
              inputStyle={signup.inputStyle}
              inputContainerStyle={signup.inputContainerStyle}
            />
            <Input
              placeholder="Surname"
              inputStyle={signup.inputStyle}
              inputContainerStyle={signup.inputContainerStyle}
            />
            <CustomDatePicker placeholder="Date of Birth"/>
            <Input
              placeholder="Email"
              inputStyle={signup.inputStyle}
              inputContainerStyle={signup.inputContainerStyle}
            />
            <Input
              placeholder="Password"
              secureTextEntry={true}
              inputStyle={signup.inputStyle}
              inputContainerStyle={signup.inputContainerStyle}
              rightIcon={
                <Icon
                  name="eye"
                  type="font-awesome"
                  size={24}
                  color={colors.ocean1}
                />
              }
            />
            <Input
              placeholder="Password Confirmation"
              secureTextEntry={true}
              inputStyle={signup.inputStyle}
              inputContainerStyle={signup.inputContainerStyle}
              rightIcon={
                <Icon
                  name="eye"
                  type="font-awesome"
                  size={24}
                  color={colors.ocean1}
                />
              }
            />
            <CheckBox
              title="I have read the User Agreement"
              checked={true}
              fontFamily="Gill Sans"
              containerStyle={signup.checkBoxContainerStyle}
            />
            <Button
              titleStyle={signup.subText}
              buttonStyle={signup.buttonStyle}
              title="SIGN UP"
              onPress={() => this.props.navigation.navigate('SIGN UP')}
            />
          </View>
          <View style={signup.flex1} />
        </ScrollView>
      </View>
    );
  }
}
export default SignUpScreen;
