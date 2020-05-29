import React from 'react';
import {ScrollView, StyleSheet, Text, View} from 'react-native';
import {Icon, Button, Overlay} from 'react-native-elements';

export interface IProps {
  title?: string;
  visible: boolean;
  onBackdropPress: () => any;
  overlayStyle: any;
  children: any;
}

interface IState {}

export class CustomModal extends React.Component<IProps, IState> {
  constructor(props: IProps) {
    super(props);
  }

  render() {
    const {children, visible, onBackdropPress, overlayStyle} = this.props;
    return (     
      <Overlay 
        isVisible={visible} 
        onBackdropPress={onBackdropPress} 
        overlayStyle={overlayStyle}
      >
        <ScrollView>
          <Icon name='close' type='font-awesome' onPress={onBackdropPress} iconStyle={{ paddingHorizontal: 10, alignSelf: 'flex-end'}}/>
          <View style={{flex: 1, flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between'}}>
            <Text style={styles.mainText}>{this.props.title || ""}</Text>
          </View>
          {children}
        </ScrollView>
      </Overlay>
    );
  }
}
const styles= StyleSheet.create({
  mainText: {
    marginHorizontal: 10,
    marginVertical: 20,
    fontFamily: 'Gill Sans',
    fontSize: 25,
  },
});
