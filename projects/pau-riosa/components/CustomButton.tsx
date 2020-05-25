import React from 'react';
import {StyleSheet, Text, View} from 'react-native';
import {Button} from 'react-native-elements';

export interface IProps {
  title: string;
  type: any;
  titleStyle?: object;
  buttonStyle?: object;
  navigation?: any;
  handlePress?: () => any;
}

interface IState {}

export class CustomButton extends React.Component<IProps, IState> {
  constructor(props: IProps) {
    super(props);
  }

  render() {
    return (     
      <>   
        <Button
          title={this.props.title}
          type={this.props.type}
          titleStyle={this.props.titleStyle}
          buttonStyle={this.props.buttonStyle}
          onPress={() => this.props.handlePress}
        />
      </>
    );
  }
}
// styles
const styles = StyleSheet.create({
  root: {
    alignItems: 'center',
    alignSelf: 'center',
  },
  buttons: {
    flexDirection: 'row',
    minHeight: 70,
    alignItems: 'stretch',
    alignSelf: 'center',
    borderWidth: 5,
  },
  button: {
    flex: 1,
    paddingVertical: 0,
  },
  greeting: {
    color: '#999',
    fontWeight: 'bold',
  },
});
