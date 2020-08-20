import React, {Component} from 'react';
import {View, StyleSheet, NativeModules, Button} from 'react-native';

export default class Apps extends Component {
  render() {
    return (
      <View style={styles.container}>
        <Button
          style={styles.input}
          title="Navigate to whatsapp Home Activity"
          onPress={() => {
            NativeModules.WhatsAppModule.NavigatetoWhatsapp();
          }}
        />
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    marginTop: 50,
    padding: 30,
  },
  input: {
    alignItems: 'center',
    justifyContent: 'center',
    width: 250,
    height: 44,
    padding: 10,
    margin: 20,
    backgroundColor: '#D3D3D3',
  },
});
